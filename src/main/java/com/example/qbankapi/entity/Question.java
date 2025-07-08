package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "question_tbl")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", columnDefinition = "MEDIUMTEXT")
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_option_tbl",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_value", columnDefinition = "MEDIUMTEXT")
    private List<String> options;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "correct_answer")
    private Option correctAnswer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "complexity")
    private Complexity complexity;

    @Column(name = "marks")
    private Long marks;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @ToString.Exclude
    private Subject subject;

    @ManyToMany(mappedBy = "questions")
    @ToString.Exclude
    private List<Exam> associatedExams;

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private List<ParticipantUserExamQuestionAnswer> participantUserExamQuestionAnswers;

    @ManyToOne
    @JoinColumn(name = "created_by_base_user_id")
    @ToString.Exclude
    private BaseUser createdByBaseUser;

    public enum Complexity {
        EASY, MEDIUM, HARD,
    }

    public enum Option {
        A, B, C, D,
    }

}
