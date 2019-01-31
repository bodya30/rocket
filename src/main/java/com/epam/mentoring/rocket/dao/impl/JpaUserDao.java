package com.epam.mentoring.rocket.dao.impl;


import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Profile("jpa")
@Repository
public class JpaUserDao implements UserDao {

    private static final String SELECT_USER_BY_EMAL = "SELECT u FROM User u WHERE u.email LIKE :email";
    private static final String SELECT_ALL_USERS = "SELECT u FROM User u";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(SELECT_USER_BY_EMAL, User.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(SELECT_ALL_USERS).getResultList();
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
