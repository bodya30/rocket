package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultVerificationTokenService implements VerificationTokenService {

    @Autowired
    private VerificationTokenDao tokenDao;

    @Autowired
    private UserDao userDao;

    @Override
    public VerificationToken getByToken(String token) {
        VerificationToken verificationToken = tokenDao.getByToken(token);
        User user = userDao.getUserById(verificationToken.getUser().getId());
        verificationToken.setUser(user);
        return verificationToken;
    }

    @Override
    public VerificationToken insertToken(VerificationToken token) {
        return tokenDao.insertToken(token);
    }



    public VerificationTokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(VerificationTokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }
}
