package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Profile("jdbc")
@Service
@Transactional
public class DefaultUserService extends AbstractUserService {

    private static final AuthorityName DEFAULT_AUTHORITY_NAME = AuthorityName.ROLE_USER;

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(getUserDao().getUserById(id)).map(this::setUserAuthorities);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(getUserDao().getUserByEmail(email)).map(this::setUserAuthorities);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = getUserDao().getAllUsers();
        if (isNotEmpty(users)) {
            users.forEach(this::setUserAuthorities);
        }
        return users;
    }

    private User setUserAuthorities(User user) {
        Set<Authority> authorities = getAuthorityDao().getAuthoritiesByUserId(user.getId());
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public User insertUser(User user) {
        String email = user.getEmail();
        if (getUserByEmail(email).isPresent()) {
            throw new EmailExistsException("User with email" + email + "already exists");
        }
        encodeUserPassword(user);
        User insertedUser = getUserDao().insertUser(user);
        insertUserAuthorities(user);
        getTokenService().insertTokenForUser(user);
        return insertedUser;
    }

    private void insertUserAuthorities(User user) {
        Set<Authority> authorities = user.getAuthorities();
        if (isNotEmpty(authorities)) {
            authorities.forEach(authority -> getAuthorityDao().insertAuthorityForUser(authority, user));
        } else {
            Authority defaultAuthority = getAuthorityDao().getAuthorityByName(DEFAULT_AUTHORITY_NAME);
            getAuthorityDao().insertAuthorityForUser(defaultAuthority, user);
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
            emptyIfNull(user.getAuthorities()).forEach(authority -> getAuthorityDao().removeAuthorityForUser(authority, user));
            getTokenService().removeTokenForUser(user);
            getUserDao().removeUser(id);
        }
    }

}
