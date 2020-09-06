package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<QuestionResponse> createQuestion(
            @RequestHeader("authorization") final String accessToken,
            final QuestionRequest request ) {

        Question question = new Question();
        question.setContent(request.getContent());
        question.setUuid(UUID.randomUUID().toString());
        question.setDate(Instant.now());

        try {
            question = questionService.createQ(accessToken, question);

            return new ResponseEntity<>(
                    new QuestionResponse()
                            .id(question.getUuid())
                            .status("QUESTION CREATED"),
                    HttpStatus.CREATED
                    );
        } catch (AuthorizationFailedException e) {
            return new ResponseEntity<>(
                    new QuestionResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "/question/all"
    )
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
            @RequestHeader("authorization") final String accessToken) {

        try {
            final List<Question> questions = questionService.getAllQ(accessToken);

            final List<QuestionDetailsResponse> responseList = new ArrayList<>();

            Optional.ofNullable(questions)
                    .orElse(Collections.emptyList())
                    .forEach(e -> {
                        QuestionDetailsResponse response = new QuestionDetailsResponse()
                                .id(e.getUuid())
                                .content(e.getContent());
                        responseList.add(response);
                    });

            return new ResponseEntity<>(responseList, HttpStatus.OK);


        } catch (AuthorizationFailedException e) {
            return null;
        }
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/question/edit/{questionId}"
    )
    public ResponseEntity<QuestionEditResponse> editQuestionContent(
            @Valid @PathVariable final String questionId,
            final QuestionEditRequest request,
            @RequestHeader("authorization") final String accessToken) {


        try {
            final String responseCode = questionService.editQContent(questionId, request.getContent(), accessToken);

            QuestionEditResponse response = new QuestionEditResponse()
                    .id(responseCode)
                    .status("QUESTION EDITED");

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );
        } catch (AuthorizationFailedException e) {
            return new ResponseEntity<>(
                    new QuestionEditResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.FORBIDDEN
            );
        } catch (InvalidQuestionException e) {
            return new ResponseEntity<>(
                    new QuestionEditResponse()
                            .id(e.getCode())
                            .status(e.getErrorMessage()),
                    HttpStatus.UNAUTHORIZED
            );
        }

    }



}
