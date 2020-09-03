package com.upgrad.quora.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;
    private Integer user_id;
    private String access_token;

    private Instant expiresAt;
    private Instant loginAt;
    private Instant logoutAt;

}
