package com.epam.mentoring.rocket.facade.converter.token;

import com.epam.mentoring.rocket.dto.VerificationTokenData;
import com.epam.mentoring.rocket.facade.converter.Converter;
import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenConverter extends Converter<VerificationToken, VerificationTokenData> {

    @Override
    VerificationTokenData convert(VerificationToken token);
}
