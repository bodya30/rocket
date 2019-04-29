package com.epam.mentoring.rocket.controller;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.dto.VerificationTokenData;
import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.facade.UserFacade;
import com.epam.mentoring.rocket.facade.VerificationTokenFacade;
import com.epam.mentoring.rocket.form.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.mentoring.rocket.controller.ControllerConstants.REDIRECT_PREFIX;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final String CONFIRMATION_FAILED_URL = "/registration/confirm-failed";
    private static final String HOST_HEADER = "Host";
    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private VerificationTokenFacade tokenFacade;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @GetMapping
    public String showPage(HttpServletRequest request, Model model) {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletRequestAttributes(request));
        model.addAttribute("connection", connection);
        return "registration";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm registrationForm,
                                          BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<?> response;
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getAllErrors().stream()
                    .collect(groupingBy(this::getErrorKey,
                            mapping(ObjectError::getDefaultMessage, toList())));
            response = ResponseEntity.badRequest().body(errors);
        } else {
            response = registerUser(registrationForm, request);
        }
        return response;
    }

    private String getErrorKey(ObjectError error) {
        String key = error.getObjectName();
        if (error instanceof FieldError) {
            key = ((FieldError) error).getField();
        }
        return key;
    }

    private ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm registrationForm, HttpServletRequest req) {
        ResponseEntity<?> response;
        UserData userAccount = createUserAccount(registrationForm);
        if (userAccount == null) {
            response = ResponseEntity.badRequest().body(singletonMap("email", asList("This email is already used")));
        } else {
            tokenFacade.sendEmailWithToken(userAccount, getWebAppUrl(req));
            response = ResponseEntity.noContent().build();
        }
        return response;
    }

    private UserData createUserAccount(RegistrationForm registrationForm) {
        UserData registeredUser;
        try {
            UserData userData = populateUserData(registrationForm);
            registeredUser = userFacade.insertUser(userData);
        } catch (EmailExistsException ex) {
            registeredUser = null;
        }
        return registeredUser;
    }

    private UserData populateUserData(RegistrationForm form) {
        UserData userData = new UserData();
        userData.setFirstName(form.getFirstName());
        userData.setLastName(form.getLastName());
        userData.setEmail(form.getEmail());
        userData.setPassword(form.getPassword());
        return userData;
    }

    private String getWebAppUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getHeader(HOST_HEADER);
        String contextPath = request.getContextPath();
        return scheme + "://" + host + contextPath;
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token, RedirectAttributes attributes) {
        String resultView = REDIRECT_PREFIX + CONFIRMATION_FAILED_URL;
        Optional<VerificationTokenData> tokenOptional = tokenFacade.getByToken(token);
        if (tokenOptional.isPresent()) {
            VerificationTokenData verificationToken = tokenOptional.get();
            resultView = checkTokenExpiryDate(verificationToken, attributes);
        } else {
            attributes.addFlashAttribute(MESSAGE_KEY, "Verification token is invalid");
        }
        return resultView;
    }

    private String checkTokenExpiryDate(VerificationTokenData verificationToken, RedirectAttributes attributes) {
        String resultView = REDIRECT_PREFIX + CONFIRMATION_FAILED_URL;
        if (tokenFacade.checkTokenExpirationDate(verificationToken)) {
            userFacade.activateUser(verificationToken.getUser());
            resultView = REDIRECT_PREFIX + "/login";
        } else {
            attributes.addFlashAttribute(MESSAGE_KEY, "Verification token expired");
        }
        return resultView;
    }

    @GetMapping("/confirm-failed")
    public String showConfirmationFailed(Model model) {
        model.addAttribute(STATUS_KEY, HttpStatus.BAD_REQUEST.value());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        model.addAttribute(STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute(MESSAGE_KEY, ex.getMessage());
        return REDIRECT_PREFIX + "error";
    }
}
