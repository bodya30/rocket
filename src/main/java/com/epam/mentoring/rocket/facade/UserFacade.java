package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserDto;

import java.util.List;

public interface UserFacade {

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void insertUser(UserDto user);

    void updateUser(UserDto user);

    void removeUser(Long id);
}
