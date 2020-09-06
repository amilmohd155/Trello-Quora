package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Question createQuestion(Question question) {
        entityManager.persist(question);
        return question;
    }

    public Question getQuestionByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery(
                    "questionByUuid",
                    Question.class
            ).setParameter(
                    "uuid",
                    uuid
            ).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }

}
