package com.epam.mentoring.rocket.facade.converter.user.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.facade.converter.user.UserReverseConverter;
import com.epam.mentoring.rocket.model.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserReverseConverter implements UserReverseConverter {

    @Override
    public User reverseConvert(UserData userData) {
        User user = new User();
        user.setId(userData.getId());
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
        return user;
    }
}
