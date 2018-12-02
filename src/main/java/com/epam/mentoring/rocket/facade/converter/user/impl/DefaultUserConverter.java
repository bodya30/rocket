package com.epam.mentoring.rocket.facade.converter.user.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.facade.converter.user.UserConverter;
import com.epam.mentoring.rocket.model.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserConverter implements UserConverter {

    @Override
    public UserData convert(User user) {
        UserData userData = new UserData();
        userData.setId(user.getId());
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        userData.setEmail(user.getEmail());
        userData.setPassword(user.getPassword());
        return userData;
    }
}
