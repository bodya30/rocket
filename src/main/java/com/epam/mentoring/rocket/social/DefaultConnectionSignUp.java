package com.epam.mentoring.rocket.social;

import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.AuthorityService;
import com.epam.mentoring.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Collections.singleton;

@Component
public class DefaultConnectionSignUp implements ConnectionSignUp {

    private static final String EMPTY = "";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    /*GitHub user email may be null until
    user set it accessible in his profile settings.
    In this case just return null to indicate an implicit
    local user profile could not be created */

    public String execute(Connection<?> connection) {
        String userId = null;
        UserProfile profile = connection.fetchUserProfile();
        if (profile.getEmail() != null) {
            User user = createUserFromProfile(profile);
            User insertedUser = userService.insertUser(user);
            userId = insertedUser.getId().toString();
        }
        return userId;
    }

    private User createUserFromProfile(UserProfile profile) {
        User user = new User();
        setName(profile.getFirstName(), user::setFirstName);
        setName(profile.getLastName(), user::setLastName);
        user.setEmail(profile.getEmail());
        user.setPassword(UUID.randomUUID().toString());
        user.setEnabled(true);
        Authority authority = authorityService.getAuthorityByName(AuthorityName.ROLE_SOCIAL_USER);
        user.setAuthorities(singleton(authority));
        return user;
    }

    private void setName(String name, Consumer<String> consumer) {
        if (name == null) {
            consumer.accept(EMPTY);
        } else {
            consumer.accept(name);
        }
    }

}