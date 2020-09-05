package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdminService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    public User getUser(final String uuid, final String authToken)
            throws UserNotFoundException, AuthorizationFailedException {

        UserAuth auth = userDao.getUserAuthToken(authToken);
        String role = auth.getUser().getRole();

        if(role != null && role.equals("admin")){
            User user = userDao.getUser(uuid);
            if(user == null) throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return null;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User createUser(User user) throws SignUpRestrictedException {

        User existingUserByEmail = userDao.getUserByEmail(user.getEmail());
        if(existingUserByEmail != null)
            throw new SignUpRestrictedException(
                "SGR-002",
                "This user has already been registered, try with any other emailId"
        );

        User existingUserByUsername = userDao.getUserByUsername(user.getUsername());
        if(existingUserByUsername != null)
            throw new SignUpRestrictedException(
                    "SGR-001",
                    "Try any other Username, this Username has already been taken"
            );

        String[] encryptPassword = cryptographyProvider.encrypt(user.getPassword());
        user.setSalt(encryptPassword[0]);
        user.setPassword(encryptPassword[1]);

        return userDao.createUser(user);

    }

}
