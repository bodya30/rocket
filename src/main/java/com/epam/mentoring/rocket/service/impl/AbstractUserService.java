package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractUserService implements UserService, UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                user.isEnabled(), true, true, true,
                getGrantedAuthorities(user.getAuthorities()));
    }

    private static List<GrantedAuthority> getGrantedAuthorities(Set<Authority> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;
    }
}
