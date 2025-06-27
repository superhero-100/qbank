package com.example.qbankapi.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetailsDto {

    private Long id;

    private String description;

    private String subjectName;

    private Integer totalMarks;

    private Integer totalQuestions;

    private Integer totalEnrolledUsers;

    private LocalDateTime createdAt;

}
