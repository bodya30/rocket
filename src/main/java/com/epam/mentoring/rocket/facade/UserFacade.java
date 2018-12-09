package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserData;

import java.util.List;

public interface UserFacade {

    UserData getUserById(Long id);

    UserData getUserByEmail(String email);

    List<UserData> getAllUsers();

    UserData insertUser(UserData user);

    int updateUser(UserData user);

    int removeUser(Long id);
}
