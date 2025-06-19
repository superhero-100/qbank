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
    private Integer totalSubmissions = 0;

    @Column(name = "average_score")
    @Builder.Default
    private Double averageScore = 0D;

    @Column(name = "highest_score")
    @Builder.Default
    private Double highestScore = 0D;

    @Column(name = "lowest_score")
    @Builder.Default
    private Double lowestScore = 0D;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private Exam exam;

}
