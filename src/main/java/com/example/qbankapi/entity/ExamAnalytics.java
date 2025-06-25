package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exam_analytics_tbl")
public class ExamAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_submissions", nullable = false)
    private Integer totalSubmissions;

    @Column(name = "average_score", nullable = false)
    private Double averageScore;

    @Column(name = "highest_score", nullable = false)
    private Double highestScore;

    @Column(name = "lowest_score", nullable = false)
    private Double lowestScore;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private Exam exam;

}
