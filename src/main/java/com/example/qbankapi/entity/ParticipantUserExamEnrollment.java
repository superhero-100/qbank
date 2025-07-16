package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "participant_user_exam_enrollment_tbl")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantUserExamEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @ToString.Exclude
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "participant_user_id")
    @ToString.Exclude
    private ParticipantUser participantUser;

    @Column(name = "exam_attempt_status")
    @Enumerated(EnumType.ORDINAL)
    private ExamAttemptStatus examAttemptStatus;

    @OneToOne(mappedBy = "participantUserExamEnrollment")
    @ToString.Exclude
    private ParticipantUserExamSubmission participantUserExamSubmission;

    public enum ExamAttemptStatus {
        SUBMITTED, NOT_ATTEMPTED,
    }

}
