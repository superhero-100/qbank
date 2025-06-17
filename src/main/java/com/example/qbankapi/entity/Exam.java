package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "exam_tbl")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "total_marks")
    private Integer totalMarks;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @ToString.Exclude
    private Subject subject;

    @ManyToMany
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @Builder.Default
    @ToString.Exclude
    private List<Question> questions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "exam_user",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    @ToString.Exclude
    private List<User> enrolledUsers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "analytics_id")
    @ToString.Exclude
    private ExamAnalytics analytics;

}