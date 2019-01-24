package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Profile("springdata")
public interface SpringDataUserDao extends UserDao, CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.firstName=:#{#user.firstName}, u.lastName=:#{#user.lastName}," +
            "u.email=:#{#user.email}, u.password=:#{#user.password}, u.enabled=:#{#user.enabled} WHERE u.id=:#{#user.id}")
    void updateUser(@Param("user") User user);
}
