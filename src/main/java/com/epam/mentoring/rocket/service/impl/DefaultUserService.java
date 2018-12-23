package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.AuthorityDao;
import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.UserService;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@Transactional
public class DefaultUserService implements UserService, UserDetailsService {

    private static final AuthorityName DEFAULT_AUTHORITY_NAME = AuthorityName.ROLE_USER;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private VerificationTokenService tokenService;

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userDao.getUserById(id)).map(this::setUserAuthorities);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userDao.getUserByEmail(email)).map(this::setUserAuthorities);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        if (isNotEmpty(users)) {
            users.forEach(this::setUserAuthorities);
        }
        return users;
    }

    private User setUserAuthorities(User user) {
        List<Authority> authorities = authorityDao.getAuthoritiesByUserId(user.getId());
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public User insertUser(User user) {
        String email = user.getEmail();
        if (getUserByEmail(email).isPresent()) {
            throw new EmailExistsException("User with email" + email + "already exists");
        }
        User insertedUser = userDao.insertUser(user);
        insertUserAuthorities(user);
        tokenService.insertTokenForUser(user);
        return insertedUser;
    }

    private void insertUserAuthorities(User user) {
        List<Authority> authorities = user.getAuthorities();
        if (isNotEmpty(authorities)) {
            authorities.forEach(authority -> authorityDao.insertAuthorityForUser(authority, user));
        } else {
            Authority defaultAuthority = authorityDao.getAuthorityByName(DEFAULT_AUTHORITY_NAME);
            authorityDao.insertAuthorityForUser(defaultAuthority, user);
        }
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int removeUser(Long id) {
        int removedQuantity = 0;
        Optional<User> userOptional = getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            emptyIfNull(user.getAuthorities()).forEach(authority -> authorityDao.removeAuthorityForUser(authority, user));
            removedQuantity = userDao.removeUser(id);
        }
        return removedQuantity;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                user.isEnabled(), true, true, true,
                getGrantedAuthorities(user.getAuthorities()));
    }

    private static List<GrantedAuthority> getGrantedAuthorities(List<Authority> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public AuthorityDao getAuthorityDao() {
        return authorityDao;
    }

    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }
}
