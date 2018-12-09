package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User insertUser(User user);

    int updateUser(User user);

    int removeUser(Long id);
}
