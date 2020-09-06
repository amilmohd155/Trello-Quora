package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserAdminService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.upgrad.quora.api.util.Basic64Splitter.splitter;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserAdminService service;

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/admin/user/{userId}"
    )
    public ResponseEntity<UserDeleteResponse> deleteUser(
            @Valid @PathVariable String userId,
            @RequestHeader("authorization")final String authorization){

        final String[] decodedText = splitter(authorization);

        try {

            final String resCode = service.deleteUser(userId, decodedText[0]);
            return new ResponseEntity<>(
                    new UserDeleteResponse()
                            .id(resCode)
                            .status("USER SUCCESSFULLY DELETED"),
                    HttpStatus.OK
            );

        } catch (AuthorizationFailedException e) {
            e.printStackTrace();

            return new ResponseEntity<>(
                    new UserDeleteResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );

        } catch (UserNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity<>(
                    new UserDeleteResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );

        }

    }

}
