package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "participant_user_tbl")
@DiscriminatorValue("PARTICIPANT")
public class ParticipantUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

    @ManyToMany(mappedBy = "enrolledParticipantUsers")
    @ToString.Exclude
    private List<Exam> enrolledExams;

    @ManyToMany(mappedBy = "completedParticipantUsers")
    @ToString.Exclude
    private List<Exam> completedExams;

    @OneToMany(mappedBy = "participantUser")
    @ToString.Exclude
    private List<ParticipantUserExamResult> participantUserExamResults;

}
