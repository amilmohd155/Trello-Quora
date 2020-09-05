package com.upgrad.quora.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NamedQueries(
        {
                @NamedQuery(
                        name = "userByUuid",
                        query = "select u from User u where u.uuid = :uuid"
                ),
                @NamedQuery(
                        name = "userByEmail",
                        query = "select u from User u where u.email =:email"
                ),
                @NamedQuery(
                        name="userByUsername",
                        query = "select u from User u where u.username =:username"
                )
        }
)
public class User  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String uuid;

    @NotNull
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    private String email;

    @ToStringExclude
    private String password;

    @NotNull
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
