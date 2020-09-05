package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<QuestionResponse>> getAllQuestions(
            @RequestHeader("authorization") final String accessToken) {
        return null;
    }

}
