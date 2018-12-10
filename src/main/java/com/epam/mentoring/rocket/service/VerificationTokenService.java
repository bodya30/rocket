package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenService {

    VerificationToken getByToken(String token);

    VerificationToken insertToken(VerificationToken token);

}
