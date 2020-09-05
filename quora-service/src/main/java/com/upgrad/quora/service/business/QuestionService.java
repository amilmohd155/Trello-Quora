package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

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
                    "User is signed out.Sign in first to get user details"
            );

        return questionDao.createQuesion(question);

    }

}
