package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.UUID;

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
            @RequestHeader("authorization") final String accessToken) {

        Answer answer = new Answer();
        answer.setAns(request.getAnswer());
        answer.setUuid(UUID.randomUUID().toString());
        answer.setDate(Instant.now());

        try {
            answer = answerService.createAnswer(questionId, answer, accessToken);
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

}
