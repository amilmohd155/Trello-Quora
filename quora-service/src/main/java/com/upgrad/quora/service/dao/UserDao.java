package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public User getUser(final String uuid) {
        try {
            return entityManager.createNamedQuery(
                    "userByUuid",
                    User.class
            ).setParameter(
                    "uuid",
                    uuid
            ).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }

    public User getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery(
                    "userByEmail",
                    User.class
            ).setParameter(
                    "email",
                    email
            ).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }

    public User getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery(
                    "userByUsername",
                    User.class
            ).setParameter(
                    "username",
                    username
            ).getSingleResult();
        }catch (NoResultException nre) {
            return  null;
        }
    }

    public UserAuth createAuthToken(final UserAuth auth) {
        entityManager.persist(auth);
        return auth;
    }

    public UserAuth getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery(
                    "userAuthTokenByAccessToken",
                    UserAuth.class
            ).setParameter(
                    "accessToken",
                    accessToken
            ).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }

}
