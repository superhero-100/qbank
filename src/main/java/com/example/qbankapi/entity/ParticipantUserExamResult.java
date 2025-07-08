package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "participant_user_id")
    @ToString.Exclude
    private ParticipantUser participantUser;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @ToString.Exclude
    private Exam exam;

    @OneToOne
    @JoinColumn(name = "participant_user_exam_analytics_id")
    @ToString.Exclude
    private ParticipantUserExamAnalytics participantUserExamAnalytics;

    @OneToOne
    @JoinColumn(name = "participant_user_exam_submission_id")
    @ToString.Exclude
    private ParticipantUserExamSubmission participantUserExamSubmission;

}
