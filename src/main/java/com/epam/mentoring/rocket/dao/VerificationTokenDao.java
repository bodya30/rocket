package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenDao {

    VerificationToken getByToken(String token);

    VerificationToken insertToken(VerificationToken token);
}
