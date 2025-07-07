package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "participant_user_exam_result_tbl")
public class ParticipantUserExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "submitted_at")
    private ZonedDateTime submittedAt;

    @Column(name = "attempt_status")
    @Enumerated(EnumType.STRING)
    private ExamAttemptStatus attemptStatus;

    @ManyToOne
    @JoinColumn(name = "participant_user_id")
    @ToString.Exclude
    private ParticipantUser participantUser;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @ToString.Exclude
    private Exam exam;

    @OneToMany(mappedBy = "participantUserExamResult")
    @ToString.Exclude
    private List<ParticipantUserQuestionAnswer> answers;

    @OneToOne
    @JoinColumn(name = "user_analytics_id")
    @ToString.Exclude
    private ParticipantUserExamAnalytics analytics;

    public enum ExamAttemptStatus {
        SUBMITTED, NOT_ATTEMPTED,
    }

}
