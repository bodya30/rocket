package com.epam.mentoring.rocket.dto;

import java.util.Date;

public class VerificationTokenData {

    private String token;
    private UserData user;
    private Date expiryDate;

    public VerificationTokenData() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
