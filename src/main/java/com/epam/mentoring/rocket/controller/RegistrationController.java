package com.epam.mentoring.rocket.controller;

import com.epam.mentoring.rocket.form.RegistrationForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final String REGISTRATION_FORM = "registrationForm";

    @GetMapping
    public String showPage(Model model) {
        model.addAttribute(REGISTRATION_FORM, new RegistrationForm());
        return "registration";
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@Valid @ModelAttribute(REGISTRATION_FORM) RegistrationForm registrationForm,
                                          BindingResult bindingResult) {
        ResponseEntity<?> response = ResponseEntity.noContent().build();
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getFieldErrors().stream()
                    .collect(groupingBy(FieldError::getField,
                            mapping(FieldError::getDefaultMessage, toList())));
            response = ResponseEntity.badRequest().body(errors);
        }
        return response;
    }

}
