package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long totalParticipants;
    private Long totalSubmissions;

    private Double averageScore;
    private Double highestScore;
    private Double lowestScore;

    private Double completionRate;
    private Double passRate;

    private Long totalQuestions;

    private String createdBy;

    private ZonedDateTime createdAt;

}
