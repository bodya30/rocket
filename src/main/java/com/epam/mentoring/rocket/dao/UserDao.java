package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User insertUser(User user);

    void updateUser(User user);

    void removeUser(Long id);
}
