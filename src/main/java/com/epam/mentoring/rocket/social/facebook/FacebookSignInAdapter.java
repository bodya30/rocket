package com.epam.mentoring.rocket.social.facebook;

import com.epam.mentoring.rocket.model.AuthorityName;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import static java.util.Collections.singleton;

@Component
public class FacebookSignInAdapter implements SignInAdapter {

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(AuthorityName.ROLE_FACEBOOK_USER.name());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userId, null, singleton(authority)));
        return null;
    }
}
