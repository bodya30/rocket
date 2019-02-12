package com.epam.mentoring.rocket.facade.impl;

import com.epam.mentoring.rocket.dto.UserData;
import com.epam.mentoring.rocket.facade.converter.user.UserConverter;
import com.epam.mentoring.rocket.facade.converter.user.UserReverseConverter;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("User Facade Test")
@ExtendWith(MockitoExtension.class)
public class DefaultUserFacadeTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@email.com";

    @Mock
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserReverseConverter userReverseConverter;

    @Mock
    private User user;

    @InjectMocks
    private DefaultUserFacade unit;

    private UserData userData = new UserData();

    @BeforeEach
    public void setUp() {
        lenient().when(userConverter.convert(user)).thenReturn(userData);
        lenient().when(userReverseConverter.reverseConvert(userData)).thenReturn(user);
    }

    @DisplayName("Get user by id")
    @Test
    public void shouldGetUserById() {
        when(userService.getUserById(USER_ID)).thenReturn(Optional.of(user));

        Optional<UserData> userDataOptional = unit.getUserById(USER_ID);

        assertTrue(userDataOptional.isPresent());
        assertEquals(userData, userDataOptional.get());
    }

    @DisplayName("Get user by email")
    @Test
    public void shouldGetUserByEmail() {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        Optional<UserData> userDataOptional = unit.getUserByEmail(USER_EMAIL);

        assertTrue(userDataOptional.isPresent());
        assertEquals(userData, userDataOptional.get());
    }

    @DisplayName("Get all users")
    @Test
    public void shouldGetAllUsers() {
        int expectedQuantity = 1;
        when(userService.getAllUsers()).thenReturn(singletonList(user));

        List<UserData> userDataList = unit.getAllUsers();

        assertEquals(expectedQuantity, userDataList.size());
        assertEquals(userData, userDataList.get(0));
    }

    @DisplayName("Save user")
    @Test
    public void shouldInsertUser() {
        UserData insertedUser = new UserData();
        when(userConverter.convert(user)).thenReturn(insertedUser);
        when(userService.insertUser(user)).thenReturn(user);

        UserData actualUser = unit.insertUser(userData);

        assertEquals(insertedUser, actualUser);
    }

    @DisplayName("Update user")
    @Test
    public void shouldUpdateUser() {
        unit.updateUser(userData);

        verify(userService).updateUser(user);
    }

    @DisplayName("Remove user")
    @Test
    public void shouldRemoveUser() {
        unit.removeUser(USER_ID);

        verify(userService).removeUser(USER_ID);
    }

    @DisplayName("Set user enabled to true")
    @Test
    public void shouldActivateUser() {
        userData.setId(USER_ID);
        when(userService.getUserById(USER_ID)).thenReturn(Optional.of(user));

        unit.activateUser(userData);

        verify(user).setEnabled(true);
        verify(userService).updateUser(user);
    }

}