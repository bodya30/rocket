package com.epam.mentoring.rocket.facade.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.facade.UserFacade;
import com.epam.mentoring.rocket.facade.converter.user.UserConverter;
import com.epam.mentoring.rocket.facade.converter.user.UserReverseConverter;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
public class DefaultUserFacade implements UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserReverseConverter userReverseConverter;

    @Override
    public Optional<UserData> getUserById(Long id) {
        return userService.getUserById(id).map(userConverter::convert);
    }

    @Override
    public Optional<UserData> getUserByEmail(String email) {
        return userService.getUserByEmail(email).map(userConverter::convert);
    }

    @Override
    public List<UserData> getAllUsers() {
        return emptyIfNull(userService.getAllUsers()).stream()
                .map(userConverter::convert)
                .collect(toList());
    }

    @Override
    public UserData insertUser(UserData userData) {
        User user = userService.insertUser(userReverseConverter.reverseConvert(userData));
        return userConverter.convert(user);
    }

    @Override
    public void updateUser(UserData user) {
        userService.updateUser(userReverseConverter.reverseConvert(user));
    }

    @Override
    public void removeUser(Long id) {
        userService.removeUser(id);
    }

    @Override
    public void activateUser(UserData userData) {
        userService.getUserById(userData.getId()).ifPresent(user -> {
            user.setEnabled(true);
            userService.updateUser(user);
        });
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserConverter getUserConverter() {
        return userConverter;
    }

    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public UserReverseConverter getUserReverseConverter() {
        return userReverseConverter;
    }

    public void setUserReverseConverter(UserReverseConverter userReverseConverter) {
        this.userReverseConverter = userReverseConverter;
    }
}
