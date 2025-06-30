package com.example.qbankapi.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_tbl")
@DiscriminatorValue("USER")
public class User extends BaseUser {

    @ManyToMany(mappedBy = "enrolledUsers")
    @ToString.Exclude
    private List<Exam> enrolledExams;

    @ManyToMany(mappedBy = "completedUsers")
    @ToString.Exclude
    private List<Exam> completedExams;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UserExamResult> userExamResults;

    @Column(name = "zone_id")
    private String zoneId;

}
