package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.UUID;

import static com.upgrad.quora.api.util.Basic64Splitter.splitter;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/{questionId}/answer/create"
    )
    public ResponseEntity<AnswerResponse> createAnswer(
            @Valid @PathVariable final String questionId,
            final AnswerRequest request,
            @RequestHeader("authorization") final String authorization) {

        String[] decodedString = splitter(authorization);

        Answer answer = new Answer();
        answer.setAns(request.getAnswer());
        answer.setUuid(UUID.randomUUID().toString());
        answer.setDate(Instant.now());

        try {
            answer = answerService.createAnswer(questionId, answer, decodedString[0]);
            return new ResponseEntity<>(
                    new AnswerResponse()
                            .id(answer.getUuid())
                            .status("ANSWER CREATED"),
                    HttpStatus.CREATED
            );
        } catch (InvalidQuestionException e) {
            return new ResponseEntity<>(
                    new AnswerResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (AuthorizationFailedException e) {
            return new ResponseEntity<>(
                    new AnswerResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );
        }
    }


    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/answer/edit/{answerId}"
    )
    public ResponseEntity<AnswerEditResponse> editAnswerContent(
            @Valid @PathVariable String answerId,
            final AnswerEditRequest request,
            @RequestHeader("authorization") final String authorization){

        final String[] decodedText = splitter(authorization);

        try {
            final String responseCode = answerService.editAContent(answerId, request.getContent(), decodedText[0]);

            return new ResponseEntity<>(
                    new AnswerEditResponse()
                            .id(responseCode)
                            .status("ANSWER EDITED"),
                    HttpStatus.OK
            );

        } catch (AuthorizationFailedException e) {
            e.printStackTrace();

            return  new ResponseEntity<>(
                    new AnswerEditResponse()
                    .id(e.getCode())
                    .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );

        } catch (AnswerNotFoundException e) {
            e.printStackTrace();

            return  new ResponseEntity<>(
                    new AnswerEditResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}"
    )
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
            @Valid @PathVariable String answerId,
            @RequestHeader("authorization") final String authorization ){

        final String[] decodedText = splitter(authorization);

        try {
            final String resCode = answerService.deleteAnswer(answerId, decodedText[0]);

            return new ResponseEntity<>(
                    new AnswerDeleteResponse()
                            .id(resCode)
                            .status("ANSWER DELETED"),
                    HttpStatus.OK
            );

        } catch (AuthorizationFailedException e) {
            e.printStackTrace();

            return new ResponseEntity<>(
                    new AnswerDeleteResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );

        } catch (AnswerNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity<>(
                    new AnswerDeleteResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
            );

        }

    }


}
