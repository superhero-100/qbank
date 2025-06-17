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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    @ManyToMany(mappedBy = "enrolledUsers")
    @Builder.Default
    @ToString.Exclude
    private List<Exam> enrolledExams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<UserExamResult> userExamResults = new ArrayList<>();

}
