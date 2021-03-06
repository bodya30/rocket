package com.epam.mentoring.rocket.facade;

import com.epam.mentoring.rocket.dto.UserData;

import java.util.List;
import java.util.Optional;

public interface UserFacade {

    Optional<UserData> getUserById(Long id);

    Optional<UserData> getUserByEmail(String email);

    List<UserData> getAllUsers();

    UserData insertUser(UserData user);

    void updateUser(UserData user);

    void removeUser(Long id);

    void activateUser(UserData user);
}
