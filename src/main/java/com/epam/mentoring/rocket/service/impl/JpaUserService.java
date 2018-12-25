package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Profile("jpa")
@Service
@Transactional
public class JpaUserService extends AbstractUserService {

    @Autowired
    private UserDao jpaUserDao;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(jpaUserDao.getUserById(id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(jpaUserDao.getUserByEmail(email));
    }

    @Override
    public List<User> getAllUsers() {
        return jpaUserDao.getAllUsers();
    }

    @Override
    public User insertUser(User user) {
        String email = user.getEmail();
        if (getUserByEmail(email).isPresent()) {
            throw new EmailExistsException("User with email" + email + "already exists");
        }
        encodeUserPassword(user);
        // TODO: 25.12.2018 Manage User - Authority relation
        User insertedUser = jpaUserDao.insertUser(user);
        tokenService.insertTokenForUser(user);
        return insertedUser;
    }


    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public void updateUser(User user) {
        jpaUserDao.updateUser(user);
    }

    @Override
    public void removeUser(Long id) {
        Optional<User> userOptional = getUserById(id);
        if (userOptional.isPresent()) {
            jpaUserDao.removeUser(userOptional.get().getId());
        }
    }

    public UserDao getJpaUserDao() {
        return jpaUserDao;
    }

    public void setJpaUserDao(UserDao jpaUserDao) {
        this.jpaUserDao = jpaUserDao;
    }

    public VerificationTokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
