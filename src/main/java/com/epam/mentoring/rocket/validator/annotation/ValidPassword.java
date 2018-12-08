package com.epam.mentoring.rocket.validator.annotation;


import com.epam.mentoring.rocket.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {

    String message() default "Password must contain at least 8 characters in length, 1 uppercase, 1 lowercase, 1 digit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
