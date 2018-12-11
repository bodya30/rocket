package com.epam.mentoring.rocket.facade.converter.token.impl;

import com.epam.mentoring.rocket.dto.VerificationTokenData;
import com.epam.mentoring.rocket.facade.converter.token.VerificationTokenConverter;
import com.epam.mentoring.rocket.facade.converter.user.UserConverter;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DefaultVerificationTokenConverter implements VerificationTokenConverter {

    @Autowired
    private UserConverter userConverter;

    @Override
    public VerificationTokenData convert(VerificationToken token) {
        VerificationTokenData tokenData = new VerificationTokenData();
        tokenData.setToken(token.getToken());
        tokenData.setUser(userConverter.convert(token.getUser()));
        tokenData.setExpiryDate(token.getExpiryDate());
        return tokenData;
    }
}
