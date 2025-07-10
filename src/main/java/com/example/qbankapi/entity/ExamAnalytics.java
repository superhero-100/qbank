package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exam_analytics_tbl")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExamAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "enrollments_count")
    private Integer enrollmentsCount;

    @Column(name = "submissions_count")
    private Integer submissionsCount;

    @Column(name = "average_score")
    private Double averageScore;

    @Column(name = "highest_score")
    private Double highestScore;

    @Column(name = "lowest_score")
    private Double lowestScore;

    @OneToOne(mappedBy = "examAnalytics")
    @ToString.Exclude
    private Exam exam;

}
