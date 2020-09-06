package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

import static com.upgrad.quora.api.util.Basic64Splitter.splitter;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthService authService;

    //signup request
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signup",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest request) {

        final User user = new User();

        user.setUuid(UUID.randomUUID().toString());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmailAddress());

        try {
            final User createdUser = service.signup(user);

             return new ResponseEntity<>(
                     new SignupUserResponse()
                             .id(createdUser.getUuid())
                             .status("USER SUCCESSFULLY REGISTERED"),
                     HttpStatus.CREATED
             );

        } catch (SignUpRestrictedException e) {

            return new ResponseEntity<>(
                    new SignupUserResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.CONFLICT
            );

        }

    }


    //Login request
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signin",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SigninResponse> login(@RequestHeader("authorization")final String authorization) {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodeText = new String(decode);
        String[] decodedArray = decodeText.split(":");

        try {
            final UserAuth userAuth = authService.authenticate(decodedArray[0], decodedArray[1]);

            SigninResponse response = new SigninResponse()
                    .id(userAuth.getUser().getUuid())
                    .message("SIGNED IN SUCCESSFULLY");

            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", userAuth.getAccessToken());

            return new ResponseEntity<>(response, headers, HttpStatus.OK);


        }catch (AuthenticationFailedException e) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", null);

            return new ResponseEntity<>(
                    new SigninResponse().id(e.getCode())
                            .message(e.getErrorMessage()),
                    headers,
                    HttpStatus.UNAUTHORIZED
            );

        }

    }

    //Sign out request "/user/signout"
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signout"
    )
    public ResponseEntity<SignoutResponse> signOut(@RequestHeader("authorization")final String authorization) {

        final String[] decodedText = splitter(authorization);

        try {
            final String resCode = service.signOutUser(decodedText[0]);

            return new ResponseEntity<>(
                    new SignoutResponse().id(resCode).message("SIGNED OUT SUCCESSFULLY"),
                    HttpStatus.OK
            );

        } catch (SignOutRestrictedException e) {
            e.printStackTrace();

            return new ResponseEntity<>(
                    new SignoutResponse().id(e.getCode()).message(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
                    );

        }

    }


}
