package com.example.qbankapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamRequestDto {

    private String description;

    private Long subjectId;

    private Integer totalEasyQuestions;

    private Integer totalMediumQuestions;

    private Integer totalHardQuestions;

}
