package com.epam.mentoring.rocket.controller;

import com.epam.mentoring.rocket.form.RegistrationForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping
    public String showPage() {
        return "registration";
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@Valid @RequestBody RegistrationForm registrationForm,
                                          BindingResult bindingResult) {
        ResponseEntity<?> response = ResponseEntity.noContent().build();
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getAllErrors().stream()
                    .collect(groupingBy(this::getErrorKey,
                            mapping(ObjectError::getDefaultMessage, toList())));
            response = ResponseEntity.badRequest().body(errors);
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

}
