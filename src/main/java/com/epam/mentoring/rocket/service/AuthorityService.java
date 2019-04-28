package com.epam.mentoring.rocket.service;

import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;

public interface AuthorityService {

    Authority getAuthorityByName(AuthorityName authorityName);
}
