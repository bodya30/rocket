package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    Optional<VerificationToken> getByToken(String token);

    void sendTokenToUser(User user, String appUrl);
}
