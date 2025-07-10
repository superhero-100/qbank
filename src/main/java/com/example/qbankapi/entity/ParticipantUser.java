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
@EqualsAndHashCode(callSuper = true)
public class ParticipantUser extends BaseUser {

    @Column(name = "zone_id")
    private String zoneId;

    @OneToMany(mappedBy = "participantUser")
    @ToString.Exclude
    private List<ParticipantUserExamEnrollment> examEnrollments;

    @OneToMany(mappedBy = "participantUser")
    @ToString.Exclude
    private List<ParticipantUserExamSubmission> participantUserExamSubmissions;

    @OneToMany(mappedBy = "participantUser")
    @ToString.Exclude
    private List<ParticipantUserExamResult> participantUserExamResults;

}
