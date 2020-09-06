package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.IAnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private IAnswerDao iAnswerDao;

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public Answer createAnswer(String questionUuid, Answer answer, String accessToken)
            throws InvalidQuestionException, AuthorizationFailedException {

        UserAuth userAuth = userDao.getUserAuthToken(accessToken);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );
        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to post an answer"
            );

        Question question = questionDao.getQuestionByUuid(questionUuid);

        if(question == null)
            throw new InvalidQuestionException(
                    "QUES-001",
                    "The question entered is invalid"
            );

        answer.setQuestion(question);

        return answerDao.storeAnswer(answer);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String editAContent(String uuid, String ans, String accessCode)
            throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuth userAuth = userDao.getUserAuthToken(accessCode);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );

        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to edit an answer"
            );

        final Optional<Answer> answer = iAnswerDao.findByUuid(uuid);

        if(!answer.isPresent())
            throw new AnswerNotFoundException(
                    "ANS-001",
                    "Entered answer uuid does not exist"
            );

        if(!userAuth.getUser().getUuid()
                .equals(answer.get().getUser().getUuid()))
            throw new AuthorizationFailedException(
                    "ATHR-003",
                    "Only the answer owner can edit the answer"
            );
        answer.get().setAns(ans);
        iAnswerDao.save(answer.get());

        return answer.get().getUuid();

    }

    @Transactional
    public String deleteAnswer(String uuid, String accessToken)
            throws AuthorizationFailedException,AnswerNotFoundException {

        UserAuth userAuth = userDao.getUserAuthToken(accessToken);

        if(userAuth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
            );

        if(userAuth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to delete an answer"
            );

        Optional<Answer> answer = iAnswerDao.findByUuid(uuid);

        if(!answer.isPresent())
            throw new AnswerNotFoundException(
                    "ANS-001",
                    "Entered answer uuid does not exist"
            );

        if(!userAuth.getUser().getUuid()
                .equals(answer.get().getUser().getUuid())
                ||
                !userAuth.getUser().getRole()
                        .equals("admin")
        ) throw new AuthorizationFailedException(
                    "ATHR-003",
                    "Only the answer owner or admin can delete the answer"
            );

        iAnswerDao.deleteById(answer.get().getId());

        return answer.get().getUuid();

    }

}

















