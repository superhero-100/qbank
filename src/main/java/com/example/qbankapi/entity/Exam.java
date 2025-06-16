package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_tbl")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Long totalMarks;

    @ManyToOne
    private Subject subject;

    @OneToMany
    private List<Question> questions;

    @ManyToMany
    private List<User> enrolledUsers;

    @OneToOne
    private ExamAnalytics analytics;

}