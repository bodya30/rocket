package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationToken insertTokenForUser(User user);

    Optional<VerificationToken> getTokenByTokenString(String token);

    Optional<VerificationToken> getTokenByUser(User user);

    void removeTokenForUser(User user);

    void sendTokenToUser(User user, String appUrl);
}
