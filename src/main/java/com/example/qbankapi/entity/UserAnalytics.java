package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_analytics_tbl")
public class UserAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempted_questions", nullable = false)
    private int attemptedQuestions;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers;

    @Column(name = "accuracy", nullable = false)
    private double accuracy;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private UserExamResult userExamResult;

}
