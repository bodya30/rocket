package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Profile("jpa")
@Service
@Transactional
public class JpaVerificationTokenService extends AbstractVerificationTokenService {

    @Override
    public Optional<VerificationToken> getTokenByTokenString(String token) {
        return Optional.ofNullable(getTokenDao().getTokenByTokenString(token));
    }

    @Override
    public Optional<VerificationToken> getTokenByUser(User user) {
        return Optional.ofNullable(getTokenDao().getTokenByUser(user));
    }

}
