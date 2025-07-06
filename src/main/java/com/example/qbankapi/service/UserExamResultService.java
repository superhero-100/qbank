package com.example.qbankapi.service;

import com.example.qbankapi.dao.UserExamResultDao;
import com.example.qbankapi.dto.view.ParticipantUserEnrolledExamDetailsViewDto;
import com.example.qbankapi.dto.view.ParticipantUserExamResultViewDto;
import com.example.qbankapi.entity.UserExamResult;
import com.example.qbankapi.exception.base.impl.UserExamResultNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserExamResultService {

    private final UserExamResultDao userExamResultDao;

    @Transactional
    public ParticipantUserExamResultViewDto getUserExamResultInDtoById(Long id) {
        // ex name
        UserExamResult userExamResult = userExamResultDao.findById(id).orElseThrow(() -> new UserExamResultNotFoundException(String.format("UserExamResult not found with id: %d", id)));
        return ParticipantUserExamResultViewDto.builder()
                .id(userExamResult.getId())
                .username(userExamResult.getParticipantUser().getUsername())
                .totalScore(userExamResult.getTotalScore())
                .submittedAt(userExamResult.getSubmittedAt())
                .exam(ParticipantUserExamResultViewDto.ExamViewDto.builder()
                        .id(userExamResult.getExam().getId())
                        .description(userExamResult.getExam().getDescription())
                        .totalMarks(userExamResult.getExam().getTotalMarks())
                        .subjectName(userExamResult.getExam().getSubject().getName())
                        .totalQuestions(userExamResult.getExam().getQuestions().size())
                        .totalEnrolledUsers(userExamResult.getExam().getEnrolledParticipantUsers().size())
                        .build())
                .answers(userExamResult.getAnswers().stream()
                        .map(userAnswer -> ParticipantUserExamResultViewDto.ParticipantUserAnswerViewDto.builder()
                                .id(userAnswer.getId())
                                .answerGiven(userAnswer.getAnswerGiven())
                                .isCorrect(userAnswer.getIsCorrect())
                                .question(ParticipantUserExamResultViewDto.ExamQuestionViewDto.builder()
                                        .id(userAnswer.getQuestion().getId())
                                        .text(userAnswer.getQuestion().getText())
                                        .options(userAnswer.getQuestion().getOptions())
                                        .correctAnswer(userAnswer.getQuestion().getCorrectAnswer())
                                        .complexity(userAnswer.getQuestion().getComplexity())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .analytics(ParticipantUserExamResultViewDto.ParticipantUserAnalyticsViewDto.builder()
                        .id(userExamResult.getAnalytics().getId())
                        .attemptedQuestions(userExamResult.getAnalytics().getAttemptedQuestions())
                        .correctAnswers(userExamResult.getAnalytics().getCorrectAnswers())
                        .accuracy(userExamResult.getAnalytics().getAccuracy())
                        .build())
                .build();
    }

}
