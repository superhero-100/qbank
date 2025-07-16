package com.example.qbankapi.dto.view;

import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnalyticsViewDto {

    private Long questionId;
    private String questionText;
    private String subjectName;

    private Question.Complexity complexity;
    private Long marks;
    private Boolean isActive;

    private Integer totalAttempts;
    private Integer correctAttempts;
    private Integer incorrectAttempts;

    private Double percentCorrect;
    private Double percentIncorrect;

    private ZonedDateTime createdAt;

    private String createdByUsername;

}
