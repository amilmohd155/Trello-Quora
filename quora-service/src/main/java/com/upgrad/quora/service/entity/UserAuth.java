package com.upgrad.quora.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.Instant;



@Data
@Entity
@Table(name = "user_auth")
@NamedQueries({
        @NamedQuery(
                name = "userAuthTokenByAccessToken",
                query = "select ut from UserAuth ut where ut.accessToken =:accessToken"
        )
})
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "access_token")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @NotNull
    private Instant expiresAt;

    @NotNull
    private Instant loginAt;

    @Null
    private Instant logoutAt;

}
