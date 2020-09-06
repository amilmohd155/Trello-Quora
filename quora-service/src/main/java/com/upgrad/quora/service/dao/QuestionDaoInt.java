package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionDaoInt extends JpaRepository<Question, Integer> {

    List<Question> findAllByUser(User user);

    Optional<Question> findByUuid(String uuid);

}
