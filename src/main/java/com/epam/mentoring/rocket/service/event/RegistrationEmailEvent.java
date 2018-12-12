package com.epam.mentoring.rocket.service.event;

import org.springframework.context.ApplicationEvent;

public class RegistrationEmailEvent extends ApplicationEvent {

    private String email;
    private String confirmationUrl;

    public RegistrationEmailEvent(String email, String confirmationUrl) {
        super(email);
        this.email = email;
        this.confirmationUrl = confirmationUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationUrl() {
        return confirmationUrl;
    }

    public void setConfirmationUrl(String confirmationUrl) {
        this.confirmationUrl = confirmationUrl;
    }
}
