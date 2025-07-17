package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnalyticsViewDto {

    private Long examId;
    private String examDescription;
    private String subjectName;

    private Integer totalMarks;
    private Integer totalParticipants;
    private Integer totalSubmissions;

    private Double averageScore;
    private Integer highestScore;
    private Integer lowestScore;

    private Integer totalQuestions;

    private String createdBy;

    private ZonedDateTime createdAt;

    private ZonedDateTime enrollmentStartDate;
    private ZonedDateTime enrollmentEndDate;

    private ZonedDateTime examStartDate;
    private ZonedDateTime examEndDate;

}
