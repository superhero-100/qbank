package com.example.qbankapi.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserEnrolledExamDetailsViewDto {

    private Long id;

    private String description;

    private Integer totalMarks;

    private String subjectName;

    private Integer totalQuestions;

    private Integer totalEnrolledUsers;

    private Long resultId;

}
