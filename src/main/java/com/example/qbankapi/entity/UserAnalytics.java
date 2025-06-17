package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_analytics_tbl")
public class UserAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempted_questions")
    private int attemptedQuestions;

    @Column(name = "correct_answers")
    private int correctAnswers;

    private double accuracy;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private UserExamResult userExamResult;

}
