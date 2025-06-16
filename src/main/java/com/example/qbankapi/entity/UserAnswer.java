package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_answer_tbl")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerGiven;

    private boolean isCorrect;

    @ManyToOne
    private Question question;

    @ManyToOne
    private UserExamResult userExamResult;

}
