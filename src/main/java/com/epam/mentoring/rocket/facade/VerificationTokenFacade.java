package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.dto.VerificationTokenData;

public interface VerificationTokenFacade {

    VerificationTokenData getByToken(String token);

    void sendEmailWithToken(UserData user);
}
