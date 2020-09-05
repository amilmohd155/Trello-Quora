package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserDao repository;

    @GetMapping("/")
    Collection<User> getUsers() {
        return repository.findAll();
    }
}
