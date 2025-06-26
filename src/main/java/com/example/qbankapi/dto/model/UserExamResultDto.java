package com.example.qbankapi.dto.model;

import com.example.qbankapi.entity.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExamResultDto {

    private Long id;

    private String username;

    private Integer totalScore;

    private LocalDateTime submittedAt;

    private ExamDto exam;

    private List<UserAnswerDto> answers = new ArrayList<>();

    private UserAnalyticsDto analytics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAnalyticsDto {
        private Long id;

        private Integer attemptedQuestions;

        private Integer correctAnswers;

        private Double accuracy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAnswerDto {
        private Long id;

        private String answerGiven;

        private Boolean isCorrect;

        private ExamQuestionDto question;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamDto {
        private Long id;

        private String description;

        private Integer totalMarks;

        private String subjectName;

        private Integer totalQuestions;

        private Integer totalEnrolledUsers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamQuestionDto {
        private Long id;

        private String text;

        private List<String> options;

        private String correctAnswer;

        private Question.Complexity complexity;
    }
}
