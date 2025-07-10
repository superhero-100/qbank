package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "participant_user_question_answer_tbl")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantUserExamQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "answer_given")
    private Question.Option answerGiven;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private Question question;

    @ManyToOne
    @JoinColumn(name = "participant_user_exam_submission_id")
    @ToString.Exclude
    private ParticipantUserExamSubmission participantUserExamSubmission;

}