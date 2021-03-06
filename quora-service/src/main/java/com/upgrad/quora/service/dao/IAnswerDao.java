package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAnswerDao extends JpaRepository<Answer, Integer> {

    Optional<Answer> findByUuid(String uuid);

    List<Answer> findAllByQuestion(Question question);

}
