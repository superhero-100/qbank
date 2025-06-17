package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
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

    @Column(name = "total_score")
    private int totalScore;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @ToString.Exclude
    private Exam exam;

    @OneToMany(mappedBy = "userExamResult")
    @Builder.Default
    @ToString.Exclude
    private List<UserAnswer> answers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_analytics_id")
    @ToString.Exclude
    private UserAnalytics analytics;

}
