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
@Table(name = "user_answer_tbl")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_given")
    private String answerGiven;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_exam_result_id")
    @ToString.Exclude
    private UserExamResult userExamResult;

}
