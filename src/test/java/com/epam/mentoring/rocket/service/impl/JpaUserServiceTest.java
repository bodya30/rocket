package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.impl.JpaAuthorityDao;
import com.epam.mentoring.rocket.dao.impl.JpaUserDao;
import com.epam.mentoring.rocket.exception.EmailExistsException;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.epam.mentoring.rocket.model.AuthorityName.ROLE_USER;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Jpa User Service Test")
@ExtendWith(MockitoExtension.class)
public class JpaUserServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "password";

    @Mock
    private JpaUserDao userDao;

    @Mock
    private JpaAuthorityDao authorityDao;

    @Mock
    private JpaVerificationTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private User user;

    @InjectMocks
    private JpaUserService unit;

    @BeforeEach
    public void setUp() {
        lenient().when(user.getId()).thenReturn(USER_ID);
        lenient().when(user.getEmail()).thenReturn(USER_EMAIL);
        lenient().when(user.getPassword()).thenReturn(USER_PASSWORD);
    }

    @DisplayName("Get user by id")
    @Test
    public void shouldGetUserById() {
        when(userDao.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> userOptional = unit.getUserById(USER_ID);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @DisplayName("Get user by email")
    @Test
    public void shouldGetUserByEmail() {
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        Optional<User> userOptional = unit.getUserByEmail(USER_EMAIL);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @DisplayName("Get all users")
    @Test
    public void shouldGetAllUsers() {
        int expectedUserQuantity = 1;
        when(userDao.findAll()).thenReturn(singletonList(user));

        List<User> users = unit.getAllUsers();

        assertEquals(expectedUserQuantity, users.size());
        assertEquals(user, users.get(0));
    }

    @DisplayName("Insert user")
    @Test
    public void shouldInsertUser() {
        String encodedPassword = "encodedPassword";
        Authority authority = mock(Authority.class);
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(encodedPassword);
        when(user.getAuthorities()).thenReturn(emptySet());
        when(authorityDao.findByName(ROLE_USER)).thenReturn(authority);
        when(userDao.save(user)).thenReturn(user);

        User insertUser = unit.insertUser(user);

        verify(user).setPassword(encodedPassword);
        verify(user).setAuthorities(singleton(authority));
        verify(tokenService).insertTokenForUser(user);
        assertEquals(user, insertUser);
    }

    @DisplayName("Throw email exists exception")
    @Test
    public void shouldNotInsertUser() {
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        assertThrows(EmailExistsException.class, () -> unit.insertUser(user),
                "User with email" + USER_EMAIL + "already exists");
    }

    @DisplayName("Update user")
    @Test
    public void shouldUpdateUser() {
        unit.updateUser(user);

        verify(userDao).updateUser(user);
    }

    @DisplayName("Remove user")
    @Test
    public void shouldRemoveUser() {
        when(userDao.findById(USER_ID)).thenReturn(Optional.of(user));

        unit.removeUser(USER_ID);

        verify(tokenService).removeTokenForUser(user);
        verify(userDao).removeById(USER_ID);
    }

    @DisplayName("Load user by username")
    @Test
    public void shouldLoadUserByUsername() {
        Authority authority = mock(Authority.class);
        when(user.getAuthorities()).thenReturn(singleton(authority));
        when(authority.getName()).thenReturn(ROLE_USER);
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        UserDetails userDetails = unit.loadUserByUsername(USER_EMAIL);

        assertEquals(USER_EMAIL, userDetails.getUsername());
        assertEquals(USER_PASSWORD, userDetails.getPassword());
        assertEquals(ROLE_USER.name(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @DisplayName("Throw user not found exception")
    @Test
    public void shouldNotLoadUserByUsername() {
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> unit.loadUserByUsername(USER_EMAIL),
                "No user found with username: " + USER_EMAIL);
    }

}