package com.example.qbankapi.dto.view;

import com.example.qbankapi.entity.ParticipantUserExamEnrollment;
import com.example.qbankapi.entity.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantUserExamResultViewDto {

    private String username;

    private Boolean isExamSubmitted;

    private Integer totalScore;

    private ZonedDateTime submittedAt;

    private ExamViewDto exam;

    private List<ParticipantUserAnswerViewDto> answers = new ArrayList<>();

    private ParticipantUserAnalyticsViewDto analytics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantUserAnalyticsViewDto {

        private Long id;

        private Integer attemptedQuestions;

        private Integer correctAnswers;

        private Double accuracy;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantUserAnswerViewDto {

        private Long id;

        private Question.Option answerGiven;

        private Boolean isCorrect;

        private ExamQuestionViewDto question;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamViewDto {
        private Long id;

        private String description;

        private Integer totalMarks;

        private String subjectName;

        private Integer questionsCount;

        private Integer enrollmentsCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamQuestionViewDto {
        private Long id;

        private String text;

        private List<String> options;

        private Question.Option correctAnswer;

        private Question.Complexity complexity;
    }

}
