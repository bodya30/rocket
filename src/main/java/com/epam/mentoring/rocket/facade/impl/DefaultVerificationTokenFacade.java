package com.epam.mentoring.rocket.facade.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.dto.VerificationTokenData;
import com.epam.mentoring.rocket.facade.VerificationTokenFacade;
import com.epam.mentoring.rocket.facade.converter.token.VerificationTokenConverter;
import com.epam.mentoring.rocket.facade.converter.user.UserReverseConverter;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultVerificationTokenFacade implements VerificationTokenFacade {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private VerificationTokenConverter tokenConverter;

    @Autowired
    private UserReverseConverter userReverseConverter;

    @Override
    public Optional<VerificationTokenData> getByToken(String token) {
        return Optional.ofNullable(tokenService.getByToken(token)).map(tokenConverter::convert);
    }

    @Override
    public void sendEmailWithToken(UserData userData, String appUrl) {
        User user = userReverseConverter.reverseConvert(userData);
        tokenService.sendTokenToUser(user, appUrl);
    }

    public VerificationTokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }
}
