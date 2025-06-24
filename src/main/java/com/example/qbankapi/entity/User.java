package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tbl")
@DiscriminatorValue("USER")
public class User extends BaseUser{

    @ManyToMany(mappedBy = "enrolledUsers")
    @Builder.Default
    @ToString.Exclude
    private List<Exam> enrolledExams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<UserExamResult> userExamResults = new ArrayList<>();

}
