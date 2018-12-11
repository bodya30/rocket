package com.epam.mentoring.rocket.service.event;

import org.springframework.context.ApplicationEvent;

public class RegistrationEmailEvent extends ApplicationEvent {

    private String email;
    private String appUrl;
    private String token;

    public RegistrationEmailEvent(String email, String appUrl, String token) {
        super(token);
        this.email = email;
        this.appUrl = appUrl;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
