package com.epam.mentoring.rocket.facade.converter.user;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.facade.converter.Converter;
import com.epam.mentoring.rocket.model.User;

public interface UserConverter extends Converter<User, UserData> {

    @Override
    UserData convert(User source);
}
