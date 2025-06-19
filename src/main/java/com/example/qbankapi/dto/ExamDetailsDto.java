package com.example.qbankapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetailsDto {

    private Long id;

    private String description;

    private Integer totalMarks;

    private String subjectName;

    private Integer totalQuestions;

    private Integer totalEnrolledUsers;

}
