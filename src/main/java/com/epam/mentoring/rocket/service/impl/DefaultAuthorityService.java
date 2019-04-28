package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.AuthorityDao;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultAuthorityService implements AuthorityService {

    @Autowired
    private AuthorityDao authorityDao;

    @Override
    public Authority getAuthorityByName(AuthorityName authorityName) {
        return authorityDao.findByName(authorityName);
    }
}
