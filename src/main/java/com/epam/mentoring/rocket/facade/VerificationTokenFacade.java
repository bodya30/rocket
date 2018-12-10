package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenFacade {

    VerificationToken getByToken(String token);

    VerificationToken insertToken(VerificationToken token);
}
