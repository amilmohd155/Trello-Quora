package com.upgrad.quora.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "question")
@NamedQueries(
        @NamedQuery(
                name="questionByUuid",
                query = "SELECT q FROM Question q WHERE q.uuid =:uuid"
        )
)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String uuid;
    private String content;
    private Instant date;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Answer> answers;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;



}
