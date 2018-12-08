package com.epam.mentoring.rocket.form;

import com.epam.mentoring.rocket.validator.annotation.PasswordMatches;
import com.epam.mentoring.rocket.validator.annotation.ValidEmail;
import com.epam.mentoring.rocket.validator.annotation.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@PasswordMatches
public class RegistrationForm {

    @NotBlank(message = "First name must not be empty")
    @Pattern(regexp = "^\\p{L}{2,255}$", message = "First name must be between 2 and 255 consecutive letters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Pattern(regexp = "^\\p{L}{2,255}$", message = "Last name must be between 2 and 255 consecutive letters")
    private String lastName;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
