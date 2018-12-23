package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User insertUser(User user);

    int updateUser(User user);

    int removeUser(Long id);
}
