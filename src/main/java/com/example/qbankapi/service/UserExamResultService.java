package com.example.qbankapi.service;

import com.example.qbankapi.dao.UserExamResultDao;
import com.example.qbankapi.dto.UserExamResultDto;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.UserExamResult;
import com.example.qbankapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserExamResultService {

    private final UserExamResultDao userExamResultDao;

//    @Transactional
//    public UserExamResultDto getDtoById(Long id) {
//        UserExamResult userExamResult = userExamResultDao.findById(id).orElseThrow(() -> new EntityNotFoundException("UserExamResult Not Found"));
//        return UserExamResultDto.builder()
//                .id(userExamResult.getId())
//                .username(userExamResult.getUser().getUsername())
//                .totalScore(userExamResult.getTotalScore())
//                .submittedAt(userExamResult.getSubmittedAt())
//                .exam(UserExamResultDto.ExamDto.builder()
//                        .id(userExamResult.getExam().getId())
//                        .description(userExamResult.getExam().getDescription())
//                        .totalMarks(userExamResult.getExam().getTotalMarks())
//                        .subjectName(userExamResult.getExam().getSubject().getName())
//                        .totalQuestions(userExamResult.getExam().getQuestions().size())
//                        .totalEnrolledUsers(userExamResult.getExam().getEnrolledUsers().size())
//                        .build())
//                .answers(userExamResult.getAnswers().stream()
//                        .map(userAnswer -> UserExamResultDto.UserAnswerDto.builder()
//                                .id(userAnswer.getId())
//                                .answerGiven(userAnswer.getAnswerGiven())
//                                .isCorrect(userAnswer.isCorrect())
//                                .question(UserExamResultDto.ExamQuestionDto.builder()
//                                        .id(userAnswer.getQuestion().getId())
//                                        .text(userAnswer.getQuestion().getText())
//                                        .options(userAnswer.getQuestion().getOptions())
//                                        .correctAnswer(userAnswer.getQuestion().getCorrectAnswer())
//                                        .complexity(userAnswer.getQuestion().getComplexity())
//                                        .build())
//                                .build())
//                        .collect(Collectors.toList()))
//                .analytics(UserExamResultDto.UserAnalyticsDto.builder()
//                        .id(userExamResult.getAnalytics().getId())
//                        .attemptedQuestions(userExamResult.getAnalytics().getAttemptedQuestions())
//                        .correctAnswers(userExamResult.getAnalytics().getCorrectAnswers())
//                        .accuracy(userExamResult.getAnalytics().getAccuracy())
//                        .build())
//                .build();
//    }

}
