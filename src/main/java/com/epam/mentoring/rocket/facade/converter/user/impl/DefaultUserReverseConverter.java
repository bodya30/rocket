package com.epam.mentoring.rocket.facade.converter.user.impl;

import com.epam.mentoring.rocket.dto.UserDto;
import com.epam.mentoring.rocket.facade.converter.user.UserReverseConverter;
import com.epam.mentoring.rocket.model.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserReverseConverter implements UserReverseConverter {

    @Override
    public User reverseConvert(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
