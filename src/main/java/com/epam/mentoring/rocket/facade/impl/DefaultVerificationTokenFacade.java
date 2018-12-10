package com.epam.mentoring.rocket.facade.impl;

import com.epam.mentoring.rocket.facade.VerificationTokenFacade;
import com.epam.mentoring.rocket.model.VerificationToken;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultVerificationTokenFacade implements VerificationTokenFacade {

    @Autowired
    private VerificationTokenService tokenService;

    @Override
    public VerificationToken getByToken(String token) {
        return null;
    }

    @Override
    public VerificationToken insertToken(VerificationToken token) {
        return null;
    }

    public VerificationTokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }
}
