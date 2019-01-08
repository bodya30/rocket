package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Profile("jpa")
@Repository
public class JpaVerificationTokenDao implements VerificationTokenDao {

    private static final String SELECT_BY_TOKEN = "SELECT t FROM VerificationToken t WHERE t.token LIKE :token";
    private static final String SELECT_BY_USER = "SELECT t FROM VerificationToken t WHERE t.user = :user";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VerificationToken getTokenByTokenString(String token) {
        try {
            TypedQuery<VerificationToken> query = entityManager.createQuery(SELECT_BY_TOKEN, VerificationToken.class);
            query.setParameter("token", token);
            return query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public VerificationToken getTokenByUser(User user) {
        try {
            TypedQuery<VerificationToken> query = entityManager.createQuery(SELECT_BY_USER, VerificationToken.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public VerificationToken insertToken(VerificationToken token) {
        entityManager.persist(token);
        return token;
    }

    @Override
    public void removeTokenForUser(User user) {
        VerificationToken token = getTokenByUser(user);
        entityManager.remove(token);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
