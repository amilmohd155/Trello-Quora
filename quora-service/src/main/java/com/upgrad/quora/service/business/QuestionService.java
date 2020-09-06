package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.QuestionDaoInt;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionDaoInt questionDaoInt;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public Question createQ(String accessToken, Question question)
            throws AuthorizationFailedException {

        UserAuth userAuth = userDao.getUserAuthToken(accessToken);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                "ATHR-001",
                "User has not signed in"
        );
        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to post a question"
            );

        return questionDao.createQuestion(question);

    }

    public List<Question> getAllQ(String accessToken)
            throws AuthorizationFailedException{

        UserAuth userAuth = userDao.getUserAuthToken(accessToken);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );

        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to get all questions"
            );

        return questionDaoInt.findAllByUser(userAuth.getUser());

    }

    public String editQContent(String uuid, String content, String accessCode)
            throws AuthorizationFailedException, InvalidQuestionException {

        UserAuth userAuth = userDao.getUserAuthToken(accessCode);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );

        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to edit the question"
            );

        Optional<Question> question  = questionDaoInt.findByUuid(uuid);

        if(!question.isPresent())
            throw new InvalidQuestionException(
                "QUES-001",
                "Entered question uuid does not exist"
            );

        if(!userAuth.getUser().getUuid().equals(question.get().getUser().getUuid()))
            throw new AuthorizationFailedException(
                "ATHR-003",
                "Only the question owner can edit the question"
            );

        question.get().setContent(content);

        questionDaoInt.save(question.get());

        return question.get().getUuid();


    }

}
