package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static com.epam.mentoring.rocket.model.AuthorityName.ROLE_USER;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Do not use @DataJpaTest because
// https://stackoverflow.com/questions/48347088/

@SpringBootTest
@Transactional
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("jpa")
@DisplayName("Jpa User Service Integration Test")
public class JpaUserServiceIntegrationTest {

    private static final String SELECT_BY_USER = "SELECT t FROM VerificationToken t WHERE t.user = :user";
    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String INSERTED_EMAIL = "test@email.com";
    private static final String EMAIL_TO_INSERT = "test2@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JpaUserService unit;

    private User getInsertedUserFromDb() {
        return testEntityManager.find(User.class, USER_ID);
    }

    private User createTestUser() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL_TO_INSERT);
        user.setPassword(PASSWORD);
        user.setEnabled(true);
        return user;
    }

    @Test
    @DisplayName("Get user by id")
    public void shouldGetUserById() {
        User expectedUser = getInsertedUserFromDb();

        Optional<User> userOptional = unit.getUserById(USER_ID);

        assertTrue(userOptional.isPresent());
        User actualUser = userOptional.get();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Get user by email")
    public void shouldGetUserByEmail() {
        User expectedUser = getInsertedUserFromDb();

        Optional<User> userOptional = unit.getUserByEmail(INSERTED_EMAIL);

        assertTrue(userOptional.isPresent());
        User actualUser = userOptional.get();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Get all users")
    public void shouldGetAllUsers() {
        User expectedUser = getInsertedUserFromDb();

        List<User> allUsers = unit.getAllUsers();

        assertIterableEquals(singletonList(expectedUser), allUsers);
    }

    @Test
    @DisplayName("Insert user")
    public void shouldInsertUser() {
        User expectedUser = createTestUser();
        int expectedAuthorityQty = 1;

        User insertUser = unit.insertUser(expectedUser);

        User actualUser = testEntityManager.find(User.class, insertUser.getId());
        VerificationToken token = getVerificationTokenForUser(actualUser);
        assertNotNull(actualUser);
        assertNotNull(token);
        assertAll(() -> assertEquals(expectedUser.getFirstName(), actualUser.getFirstName()),
                () -> assertEquals(expectedUser.getLastName(), actualUser.getLastName()),
                () -> assertEquals(expectedUser.getEmail(), actualUser.getEmail()),
                () -> assertTrue(expectedUser.isEnabled()),
                () -> assertEquals(expectedUser.getPassword(), actualUser.getPassword()),
                () -> assertEquals(expectedAuthorityQty, actualUser.getAuthorities().size()),
                () -> assertEquals(ROLE_USER, actualUser.getAuthorities().iterator().next().getName()));
        assertEquals(expectedUser, token.getUser());
    }

    private VerificationToken getVerificationTokenForUser(User user) {
        try {
            TypedQuery<VerificationToken> query = testEntityManager.getEntityManager().createQuery(SELECT_BY_USER, VerificationToken.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Test
    @DisplayName("Update user")
    public void shouldUpdateUser() {
        User insertedUser = getInsertedUserFromDb();
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newEmail = "newUser@email.com";
        String newPassword = "newPassword";
        boolean isAcive = false;

        insertedUser.setFirstName(newFirstName);
        insertedUser.setLastName(newLastName);
        insertedUser.setEmail(newEmail);
        insertedUser.setPassword(newPassword);
        insertedUser.setEnabled(isAcive);

        unit.updateUser(insertedUser);
        User actualUser = testEntityManager.find(User.class, USER_ID);

        assertAll(() -> assertEquals(USER_ID, actualUser.getId()),
                () -> assertEquals(newFirstName, actualUser.getFirstName()),
                () -> assertEquals(newLastName, actualUser.getLastName()),
                () -> assertEquals(newEmail, actualUser.getEmail()),
                () -> assertFalse(actualUser.isEnabled()),
                () -> assertEquals(newPassword, actualUser.getPassword()));
    }

    @Test
    @DisplayName("Remove user")
    public void shouldRemoveUser() {
        User user = getInsertedUserFromDb();

        unit.removeUser(user.getId());

        assertNull(testEntityManager.find(User.class, user.getId()));
    }

    @Test
    @DisplayName("Load userDetails by email")
    void shouldLoadUserByUsername() {
        User insertedUser = getInsertedUserFromDb();

        UserDetails userDetails = unit.loadUserByUsername(INSERTED_EMAIL);

        assertEquals(insertedUser.getEmail(), userDetails.getUsername());
    }
}