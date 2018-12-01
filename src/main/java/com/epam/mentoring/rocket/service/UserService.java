package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    void insertUser(User user);

    void updateUser(User user);

    void removeUser(Long id);
}
