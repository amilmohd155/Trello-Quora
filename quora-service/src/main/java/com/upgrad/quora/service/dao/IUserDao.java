package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Long> {



}
