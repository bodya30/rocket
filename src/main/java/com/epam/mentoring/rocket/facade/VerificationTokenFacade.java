package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.dto.VerificationTokenData;

import java.util.Optional;

public interface VerificationTokenFacade {

    Optional<VerificationTokenData> getByToken(String token);

    void sendEmailWithToken(UserData user, String appUrl);
}
