package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserExamViewDto {

    private Long id;

    private String description;

    private String subjectName;

    private Integer totalMarks;

    private Integer questionsCount;

    private Integer enrollmentCount;

    private Boolean isEnrolled;

}