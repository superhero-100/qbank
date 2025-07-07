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

    @Column(name = "total_enrollments")
    private Integer totalEnrollments;

    @Column(name = "total_submissions")
    private Integer totalSubmissions;

    @Column(name = "average_score")
    private Double averageScore;

    @Column(name = "highest_score")
    private Double highestScore;

    @Column(name = "lowest_score")
    private Double lowestScore;

    @OneToOne(mappedBy = "analytics")
    @ToString.Exclude
    private Exam exam;

}
