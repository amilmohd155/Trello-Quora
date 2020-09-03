package com.upgrad.quora.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String uuid;

    @NotNull
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String salt;

    private String country;

    @Column(name = "aboutme")
    private String aboutMe;

    private String dob;

    private String role;

    @Column(name = "contactnumber", length = 30)
    private String contactNumber;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Question> questions;

}
