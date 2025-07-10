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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Exam extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
            name = "exam_associated_question_tbl",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @ToString.Exclude
    private List<Question> questions;

    @OneToMany(mappedBy = "exam")
    @ToString.Exclude
    private List<ParticipantUserExamEnrollment> participantEnrollments;

    @OneToMany(mappedBy = "exam")
    @ToString.Exclude
    private List<ParticipantUserExamSubmission> participantUserExamSubmissions;

    @OneToMany(mappedBy = "exam")
    @ToString.Exclude
    private List<ParticipantUserExamResult> participantUserExamResults;

    @OneToOne
    @JoinColumn(name = "exam_analytics_id")
    @ToString.Exclude
    private ExamAnalytics examAnalytics;

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

    @ManyToOne
    @JoinColumn(name = "created_by_base_user_id")
    @ToString.Exclude
    private BaseUser createdByBaseUser;

}