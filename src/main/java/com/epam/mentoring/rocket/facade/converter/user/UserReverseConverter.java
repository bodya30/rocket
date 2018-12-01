package com.epam.mentoring.rocket.facade.converter.user;

import com.epam.mentoring.rocket.dto.UserDto;
import com.epam.mentoring.rocket.facade.converter.ReverseConverter;
import com.epam.mentoring.rocket.model.User;

public interface UserReverseConverter extends ReverseConverter<User, UserDto> {

    @Override
    User reverseConvert(UserDto target);
}
