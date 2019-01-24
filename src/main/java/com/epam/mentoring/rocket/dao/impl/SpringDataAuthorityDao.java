package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.AuthorityDao;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("springdata")
public interface SpringDataAuthorityDao extends AuthorityDao, Repository<Authority, Long> {

    default void insertAuthorityForUser(Authority authority, User user) {
        throw new RuntimeException("Operation not supported for spring data repository");
    }

    default void removeAuthorityForUser(Authority authority, User user) {
        throw new RuntimeException("Operation not supported for spring data repository");
    }
}
