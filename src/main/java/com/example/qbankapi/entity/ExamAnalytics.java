package com.example.qbankapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_analytics_tbl")
public class ExamAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalSubmissions;

    private double averageScore;

    private double highestScore;

    private double lowestScore;

    @OneToOne
    private Exam exam;

}
