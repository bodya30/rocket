package com.epam.mentoring.rocket.facade.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.dto.VerificationTokenData;
import com.epam.mentoring.rocket.facade.VerificationTokenFacade;
import com.epam.mentoring.rocket.facade.converter.token.VerificationTokenConverter;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultVerificationTokenFacade implements VerificationTokenFacade {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private VerificationTokenConverter tokenConverter;

    @Override
    public VerificationTokenData getByToken(String token) {
        return tokenConverter.convert(tokenService.getByToken(token));
    }

    @Override
    public void sendEmailWithToken(UserData user) {
    }

    public VerificationTokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }
}
