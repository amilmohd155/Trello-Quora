package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.IQuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
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
    private IQuestionDao iQuestionDao;

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

        return iQuestionDao.findAll();

    }

    public List<Question> getAllQByUser(String uuid, String accessCode)
            throws AuthorizationFailedException, UserNotFoundException {

        UserAuth userAuth = userDao.getUserAuthToken(accessCode);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );
        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to get all questions posted by a specific user"
            );

        User user = userDao.getUser(uuid);

        if(user == null)
            throw new UserNotFoundException(
                    "USR-001",
                    "User with entered uuid whose question details are to be seen does not exist"
            );

        return iQuestionDao.findAllByUser(user);

    }

    @Transactional(propagation = Propagation.REQUIRED)
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

        Optional<Question> question  = iQuestionDao.findByUuid(uuid);

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

        iQuestionDao.save(question.get());

        return question.get().getUuid();

    }

    @Transactional
    public String deleteQuestion(String uuid, String accessCode)
            throws AuthorizationFailedException, InvalidQuestionException{

        UserAuth userAuth = userDao.getUserAuthToken(accessCode);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );

        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to delete a question"
            );

        Optional<Question> question  = iQuestionDao.findByUuid(uuid);

        if(!question.isPresent())
            throw new InvalidQuestionException(
                    "QUES-001",
                    "Entered question uuid does not exist"
            );

        if(!userAuth.getUser().getUuid()
                .equals(question.get().getUser().getUuid())
                || !userAuth.getUser().getRole()
                        .equals("admin")
        ) throw new AuthorizationFailedException(
                    "ATHR-003",
                    "Only the question owner or admin can delete the question"
            );

        iQuestionDao.deleteById(question.get().getId());

        return question.get().getUuid();

    }



}
