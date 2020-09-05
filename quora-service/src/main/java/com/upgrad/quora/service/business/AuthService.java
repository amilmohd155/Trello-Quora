package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuth authenticate(final String username, final String password)
            throws AuthenticationFailedException {

        User user = userDao.getUserByUsername(username);
        if(user == null)
            throw new AuthenticationFailedException(
                    "ATH-001",
                    "This username does not exist"
            );

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, user.getSalt());
        if(encryptedPassword.equals(user.getPassword())) {
            JwtTokenProvider tokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuth userAuth = new UserAuth();
            userAuth.setUser(user);
            final ZonedDateTime loginAt = ZonedDateTime.now();
            final ZonedDateTime expiresAt = loginAt.plus(2, HOURS);
            userAuth.setAccessToken(
                    tokenProvider.generateToken(
                            user.getUuid(),
                            loginAt,
                            expiresAt
                    )
            );

            userAuth.setLoginAt(loginAt.toInstant());
            userAuth.setExpiresAt(expiresAt.toInstant());

            userDao.createAuthToken(userAuth);

            return userAuth;
        }else
            throw new AuthenticationFailedException(
                    "ATH-002",
                    "Password failed"
            );
    }

}
