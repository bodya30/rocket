package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.AuthorityDao;
import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Profile("jpa")
@Repository
@Transactional
public class JpaAuthorityDao implements AuthorityDao {

    private static final String SELECT_AUTHORITY_BY_NAME = "SELECT a FROM Authority a WHERE a.name = :name";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDao userDao;

    @Override
    public Authority getAuthorityByName(AuthorityName name) {
        try {
            TypedQuery<Authority> query = entityManager.createQuery(SELECT_AUTHORITY_BY_NAME, Authority.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Authority> getAuthoritiesByUserId(Long id) {
        return Optional.ofNullable(userDao.getUserById(id))
                .map(User::getAuthorities)
                .orElseThrow(() -> new IllegalArgumentException("No user with id " + id + "found"));
    }

    @Override
    public void insertAuthorityForUser(Authority authority, User user) {
        user.getAuthorities().add(authority);
        userDao.updateUser(user);
    }

    @Override
    public void removeAuthorityForUser(Authority authority, User user) {
        user.getAuthorities().remove(authority);
        userDao.updateUser(user);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
