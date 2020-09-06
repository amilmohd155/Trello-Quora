package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDaoInt extends JpaRepository<User, Long> {

    List<Question> findAllByQuestions_Uuid(String uuid);

    Question findByUuid(String uuid);

}
