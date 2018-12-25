package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;

import java.util.List;

public interface AuthorityDao {

    Authority getAuthorityByName(AuthorityName name);

    List<Authority> getAuthoritiesByUserId(Long id);

    void insertAuthorityForUser(Authority authority, User user);

    void removeAuthorityForUser(Authority authority, User user);
}
