package com.epam.mentoring.rocket.facade.converter.user.impl;

import com.epam.mentoring.rocket.dto.UserDto;
import com.epam.mentoring.rocket.facade.converter.user.UserConverter;
import com.epam.mentoring.rocket.model.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserConverter implements UserConverter {

    @Override
    public UserDto convert(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
