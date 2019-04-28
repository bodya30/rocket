package com.epam.mentoring.rocket.social.facebook;

import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.AuthorityService;
import com.epam.mentoring.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Collections.singleton;

@Component
public class FacebookConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    public String execute(Connection<?> connection) {
        Facebook facebook = (Facebook) connection.getApi();
        org.springframework.social.facebook.api.User profile = facebook.fetchObject
                ("me", org.springframework.social.facebook.api.User.class, "email", "first_name", "last_name");
        User user = populateUserFromProfile(profile);
        User insertedUser = userService.insertUser(user);
        return insertedUser.getId().toString();
    }

    private User populateUserFromProfile(org.springframework.social.facebook.api.User profile) {
        User user = new User();
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setEmail(profile.getEmail());
        user.setPassword(UUID.randomUUID().toString());
        user.setEnabled(true);
        Authority authority = authorityService.getAuthorityByName(AuthorityName.ROLE_FACEBOOK_USER);
        user.setAuthorities(singleton(authority));
        return user;
    }

}