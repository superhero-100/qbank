package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_exam_result_tbl")
public class UserExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_score", nullable = false)
    private int totalScore;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Exam exam;

    @OneToMany(mappedBy = "userExamResult")
    @ToString.Exclude
    private List<UserAnswer> answers;

    @OneToOne
    @JoinColumn(name = "user_analytics_id", nullable = false)
    @ToString.Exclude
    private UserAnalytics analytics;

}
