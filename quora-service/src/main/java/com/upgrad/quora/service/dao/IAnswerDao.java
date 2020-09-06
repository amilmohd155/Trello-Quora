package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAnswerDao extends JpaRepository<Answer, Integer> {



}
