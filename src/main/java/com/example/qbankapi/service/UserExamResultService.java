package com.example.qbankapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserExamResultService {

//    private final UserExamResultDao userExamResultDao;

//    @Transactional
//    public UserExamResultDto getUserExamResultInDtoById(Long id) {
//        UserExamResult userExamResult = userExamResultDao.findById(id).orElseThrow(() -> new UserExamResultNotFoundException(String.format("UserExamResult not found with id: %d",id)));
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
//                                .isCorrect(userAnswer.getIsCorrect())
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
