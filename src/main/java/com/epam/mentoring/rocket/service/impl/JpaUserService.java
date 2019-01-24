package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.mentoring.rocket.model.AuthorityName.ROLE_USER;
import static java.util.Collections.singleton;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Profile({"jpa", "springdata"})
@Service
@Transactional
public class JpaUserService extends AbstractUserService {

    @Override
    public Optional<User> getUserById(Long id) {
        return getUserDao().findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getUserDao().findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return getUserDao().findAll();
    }

    @Override
    public User insertUser(User user) {
        String email = user.getEmail();
        if (getUserByEmail(email).isPresent()) {
            throw new EmailExistsException("User with email" + email + "already exists");
        }
        encodeUserPassword(user);
        updateUserAuthoritiesIfEmpty(user);
        User insertedUser = getUserDao().save(user);
        getTokenService().insertTokenForUser(user);
        return insertedUser;
    }

    private void updateUserAuthoritiesIfEmpty(User user) {
        if (isEmpty(user.getAuthorities())) {
            Authority authority = getAuthorityDao().findByName(ROLE_USER);
            user.setAuthorities(singleton(authority));
        }
    }

    @Override
    public void updateUser(User user) {
        getUserDao().updateUser(user);
    }

    @Override
    public void removeUser(Long id) {
        Optional<User> userOptional = getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            getTokenService().removeTokenForUser(user);
            getUserDao().removeById(user.getId());
        }
    }
}
