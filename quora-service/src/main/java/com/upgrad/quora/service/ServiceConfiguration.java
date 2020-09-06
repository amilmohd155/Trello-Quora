package com.upgrad.quora.service;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Enabling the component scan and entity scan of classes in the below mentioned "com.upgrad.quora.service" and "com.upgrad.quora.service.entity" packages respectively.
 */
@EnableJpaRepositories
@Configuration
@ComponentScan("com.upgrad.quora.service")
@EntityScan("com.upgrad.quora.service.entity")
public class ServiceConfiguration {
}
