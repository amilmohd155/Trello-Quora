package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.SignupService;
import com.upgrad.quora.service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SignupController {

    @Autowired
    private SignupService service;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/signup",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )

//    Todo fix
    public ResponseEntity<String> signup() {

        final User user = new User();

        return new ResponseEntity<String>(user.getUuid(), HttpStatus.CREATED);

    }

}
