package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenDao {

    VerificationToken getTokenByTokenString(String token);

    VerificationToken getTokenByUser(User user);

    VerificationToken insertToken(VerificationToken token);

    void removeTokenForUser(User user);
}
