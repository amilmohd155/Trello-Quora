package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    @Autowired
    private UserAdminService adminService;

    @Transactional
    public User signup(User user) throws SignUpRestrictedException {
        return adminService.createUser(user);
    }

}
