package com.example.qbankapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateExamRequestDto {

    private String description;

    private Long subjectId;

    private Integer totalEasyQuestions;

    private Integer totalMediumQuestions;

    private Integer totalHardQuestions;

}
