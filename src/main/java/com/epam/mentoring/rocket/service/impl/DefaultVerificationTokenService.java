package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Profile("jdbc")
@Service
@Transactional
public class DefaultVerificationTokenService extends AbstractVerificationTokenService {

    @Autowired
    private UserDao userDao;

    @Override
    public Optional<VerificationToken> getTokenByTokenString(String token) {
        return Optional.ofNullable(getTokenDao().findByToken(token)).map(this::setUser);
    }

    @Override
    public Optional<VerificationToken> getTokenByUser(User user) {
        return Optional.ofNullable(getTokenDao().findByUser(user)).map(this::setUser);
    }

    private VerificationToken setUser(VerificationToken token) {
        User user = userDao.findById(token.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User for token must not be empty"));
        token.setUser(user);
        return token;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
