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
public class ExamViewDto {

    private Long id;

    private String description;

    private String subjectName;

    private Integer totalMarks;

    private Integer totalQuestions;

    private Integer totalEnrolledUsers;

    private ZonedDateTime createdAt;

}
