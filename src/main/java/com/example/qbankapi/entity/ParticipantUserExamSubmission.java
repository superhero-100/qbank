package com.example.qbankapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "participant_user_exam_submission_tbl")
public class ParticipantUserExamSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_user_id")
    @ToString.Exclude
    private ParticipantUser participantUser;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @ToString.Exclude
    private Exam exam;

    @Column(name = "submitted_at")
    private ZonedDateTime submittedAt;

    @OneToOne(mappedBy = "participantUserExamSubmission")
    @ToString.Exclude
    private ParticipantUserExamEnrollment participantUserExamEnrollment;

    @OneToMany(mappedBy = "participantUserExamSubmission")
    @ToString.Exclude
    private List<ParticipantUserExamQuestionAnswer> participantUserExamQuestionAnswers;

    @OneToOne(mappedBy = "participantUserExamSubmission")
    @ToString.Exclude
    private ParticipantUserExamResult participantUserExamResult;

}
