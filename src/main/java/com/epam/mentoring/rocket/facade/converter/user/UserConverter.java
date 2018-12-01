package com.epam.mentoring.rocket.facade.converter.user;

import com.epam.mentoring.rocket.dto.UserDto;
import com.epam.mentoring.rocket.facade.converter.Converter;
import com.epam.mentoring.rocket.model.User;

public interface UserConverter extends Converter<User, UserDto> {

    @Override
    UserDto convert(User source);
}
