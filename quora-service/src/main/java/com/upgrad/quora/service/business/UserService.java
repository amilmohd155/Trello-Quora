package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAdminService adminService;

    @Transactional
    public User signup(User user) throws SignUpRestrictedException {
        return adminService.createUser(user);
    }

    public User getUser(final String uuid, final String authToken)
            throws UserNotFoundException, AuthorizationFailedException {

        UserAuth auth = userDao.getUserAuthToken(authToken);
        if(auth == null)
            throw new AuthorizationFailedException(
                    "ATHR-001",
                    "User has not signed in"
                    );
        if(auth.getLogoutAt() != null)
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to get user details"
            );

        User user = userDao.getUser(uuid);
        if(user == null)
            throw new UserNotFoundException(
                    "USR-001",
                    "User with entered uuid does not exist"
            );

        return user;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String signOutUser(String accessCode) throws SignOutRestrictedException {

        UserAuth userAuth = userDao.getUserAuthToken(accessCode);

        if(userAuth == null)
            throw new SignOutRestrictedException(
                    "SGR-001",
                    "User is not Signed in"
            );

        userAuth.setLogoutAt(Instant.now());
        userAuth.setAccessToken(null);

        userAuth = userDao.createAuthToken(userAuth);

        return userAuth.getUser().getUuid();
    }


}
