package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Jpa User Dao")
@ExtendWith(MockitoExtension.class)
public class JpaUserDaoTest {

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@email.com";
    private static final String SELECT_USER_BY_EMAL = "SELECT u FROM User u WHERE u.email LIKE :email";
    private static final String SELECT_ALL_USERS = "SELECT u FROM User u";

    @Mock
    private EntityManager entityManager;

    @Mock
    private User user;

    @InjectMocks
    private JpaUserDao unit;

    @DisplayName("Find user by id")
    @Test
    public void shouldFindById() {
        when(entityManager.find(User.class, USER_ID)).thenReturn(user);

        Optional<User> userOptional = unit.findById(USER_ID);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get(), "No user found");
    }

    @DisplayName("Find user by email")
    @Test
    public void shouldFindByEmail() {
        TypedQuery<User> query = mock(TypedQuery.class);
        when(entityManager.createQuery(SELECT_USER_BY_EMAL, User.class)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);

        Optional<User> userOptional = unit.findByEmail(USER_EMAIL);

        verify(query).setParameter("email", USER_EMAIL);
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get(), "No user found");
    }

    @DisplayName("Throw exception when no user with email found")
    @Test
    public void shouldNotFindUserByEmailAndThrowException() {
        TypedQuery<User> query = mock(TypedQuery.class);
        when(entityManager.createQuery(SELECT_USER_BY_EMAL, User.class)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        assertThrows(NoResultException.class, () -> unit.findByEmail(USER_EMAIL));
    }

    @DisplayName("Find all users")
    @Test
    public void findAll() {
        int expectedQuantity = 1;
        TypedQuery<User> query = mock(TypedQuery.class);
        when(entityManager.createQuery(SELECT_ALL_USERS)).thenReturn(query);
        when(query.getResultList()).thenReturn(singletonList(user));

        List<User> users = unit.findAll();

        assertEquals(expectedQuantity, users.size());
        assertEquals(user, users.get(0));
    }

    @DisplayName("Save user")
    @Test
    public void save() {
        User returnedUser = unit.save(user);

        verify(entityManager).persist(user);
        assertEquals(user, returnedUser);
    }

    @DisplayName("Update user")
    @Test
    public void updateUser() {
        unit.updateUser(user);

        verify(entityManager).merge(user);
    }

    @DisplayName("Remove user")
    @Test
    public void removeById() {
        when(entityManager.find(User.class, USER_ID)).thenReturn(user);

        unit.removeById(USER_ID);

        verify(entityManager).remove(user);
    }

}