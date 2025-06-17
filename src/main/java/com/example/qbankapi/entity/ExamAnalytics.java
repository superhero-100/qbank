package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_analytics_tbl")
public class ExamAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_submissions")
    @Builder.Default
    private int totalSubmissions = 0;

    @Column(name = "average_score")
    @Builder.Default
    private double averageScore = 0;

    @Column(name = "highest_score")
    @Builder.Default
    private double highestScore = 0;

    @Column(name = "lowest_score")
    @Builder.Default
    private double lowestScore = 0;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private Exam exam;

}
