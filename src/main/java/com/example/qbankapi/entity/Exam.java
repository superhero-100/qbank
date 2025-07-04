package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
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
            name = "exam_question_tbl",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @ToString.Exclude
    private List<Question> questions;

    @ManyToMany
    @JoinTable(
            name = "exam_participant_user_enrolled_tbl",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    @ToString.Exclude
    private List<ParticipantUser> enrolledParticipantUsers;

    @ManyToMany
    @JoinTable(
            name = "exam_participant_user_completed_tbl",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    @ToString.Exclude
    private List<ParticipantUser> completedParticipantUsers;

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