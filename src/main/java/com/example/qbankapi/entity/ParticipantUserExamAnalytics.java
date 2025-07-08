package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "participant_user_exam_analytics_tbl")
public class ParticipantUserExamAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempted_questions")
    private Integer attemptedQuestions;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @Column(name = "accuracy")
    private Double accuracy;

    @OneToOne(mappedBy = "participantUserExamAnalytics")
    @ToString.Exclude
    private ParticipantUserExamResult participantUserExamResult;

}
