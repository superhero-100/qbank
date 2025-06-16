package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_exam_result_tbl")
public class UserExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalScore;

    private LocalDateTime submittedAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Exam exam;

    @OneToMany
    private List<UserAnswer> answers;

    @OneToOne
    private UserAnalytics userAnalytics;

}
