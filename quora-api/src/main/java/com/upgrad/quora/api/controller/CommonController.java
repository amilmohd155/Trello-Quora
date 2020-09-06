package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.User;
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
public class CommonController {

    @Autowired
    private UserService service;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/userprofile/{userId}"
    )
    ResponseEntity<UserDetailsResponse> getUserProfile(
            @Valid @PathVariable final String userId,
            @RequestHeader("authorization") final String authorization) {

        final String[] decodedText = splitter(authorization);

        UserDetailsResponse response = new UserDetailsResponse();

        try {

            final User user = service.getUser(userId, decodedText[0]);
            response.firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .userName(user.getUsername())
                    .emailAddress(user.getEmail())
                    .aboutMe(user.getAboutMe())
                    .country(user.getCountry())
                    .contactNumber(user.getContactNumber())
                    .dob(user.getDob());

            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (AuthorizationFailedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

}
