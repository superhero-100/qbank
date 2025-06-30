package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "exam_tbl")
public class Exam extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
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
    @ToString.Exclude
    private List<Question> questions;

    @ManyToMany
    @JoinTable(
            name = "exam_user_enrolled",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    @ToString.Exclude
    private List<User> enrolledUsers;

    @ManyToMany
    @JoinTable(
            name = "exam_user_completed",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    @ToString.Exclude
    private List<User> completedUsers;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    @ToString.Exclude
    private ExamAnalytics analytics;



    // Enrollment Period
    @Column(name = "enrollment_start_date")
    private ZonedDateTime enrollmentStartDate;

    @Column(name = "enrollment_end_date")
    private ZonedDateTime enrollmentEndDate;



    // Exam Period
    @Column(name = "exam_start_date")
    private ZonedDateTime examStartDate;

    @Column(name = "exam_end_date")
    private ZonedDateTime examEndDate;

}