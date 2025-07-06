package com.example.qbankapi.dto.model;

import com.example.qbankapi.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {

    private Long id;

    private String description;

    private Integer totalMarks;

    private String subjectName;

    private Integer totalQuestions;

    private Integer totalEnrolledUsers;

    private List<ExamQuestionDto> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamQuestionDto {

        private Long id;

        private String text;

        private List<String> options;

        private Question.Option correctAnswer;

        private Question.Complexity complexity;

    }

}
