package com.epam.mentoring.rocket.social;

import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

import static java.util.Collections.singleton;

@Component
public class DefaultSignInAdapter implements SignInAdapter {

    @Value("${spring.social.user.disabled.url}")
    private String accountDisabledUrl;

    @Autowired
    private UserService userService;

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        Optional<User> userOptional = userService.getUserById(Long.valueOf(userId));
        if (userOptional.isPresent()) {
            return signInIfUserEnabled(userOptional.get());
        } else {
            return accountDisabledUrl;
        }
    }

    private String signInIfUserEnabled(User user) {
        if (user.isEnabled()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(AuthorityName.ROLE_SOCIAL_USER.name());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getId(),
                    null, singleton(authority)));
            return null;
        } else {
            return accountDisabledUrl;
        }
    }
}
