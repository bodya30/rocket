package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void updateUser(User user);

    void removeById(Long id);
}
