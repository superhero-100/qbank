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

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @ToString.Exclude
    private Exam exam;

    @OneToMany(mappedBy = "userExamResult")
    @ToString.Exclude
    private List<UserAnswer> answers;

    @OneToOne
    @JoinColumn(name = "user_analytics_id")
    @ToString.Exclude
    private UserAnalytics analytics;

}
