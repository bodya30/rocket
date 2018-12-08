package com.epam.mentoring.rocket.validator;

import com.epam.mentoring.rocket.form.RegistrationForm;
import com.epam.mentoring.rocket.validator.annotation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class MatchingPasswordValidator implements ConstraintValidator<PasswordMatches, RegistrationForm> {

    private String errorMessage;

    @Override
    public void initialize(PasswordMatches annotation) {
        errorMessage = annotation.message();
    }

    @Override
    public boolean isValid(RegistrationForm registrationForm, ConstraintValidatorContext context) {
        String password = registrationForm.getPassword();
        String confirmPassword = registrationForm.getConfirmPassword();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation().disableDefaultConstraintViolation();
        return Optional.ofNullable(password).map(confirmPassword::equals).orElse(true);
    }

}
