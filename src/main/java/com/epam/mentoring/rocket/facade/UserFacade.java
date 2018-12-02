package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserData;

import java.util.List;

public interface UserFacade {

    UserData getUserById(Long id);

    List<UserData> getAllUsers();

    void insertUser(UserData user);

    void updateUser(UserData user);

    void removeUser(Long id);
}
