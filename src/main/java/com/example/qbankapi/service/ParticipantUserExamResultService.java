package com.example.qbankapi.service;

import com.example.qbankapi.dao.ParticipantUserExamResultDao;
import com.example.qbankapi.dto.view.ParticipantUserExamResultViewDto;
import com.example.qbankapi.entity.ParticipantUserExamResult;
import com.example.qbankapi.exception.base.impl.ParticipantUserExamResultNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantUserExamResultService {

    private final ParticipantUserExamResultDao participantUserExamResultDao;

    @Transactional
    public ParticipantUserExamResultViewDto getParticipantUserExamResultInDtoById(Long id) {
        ParticipantUserExamResult participantUserExamResult = participantUserExamResultDao.findById(id).orElseThrow(() -> new ParticipantUserExamResultNotFoundException(String.format("UserExamResult not found with id: %d", id)));
        return ParticipantUserExamResultViewDto.builder()
                .id(participantUserExamResult.getId())
                .username(participantUserExamResult.getParticipantUser().getUsername())
                .totalScore(participantUserExamResult.getTotalScore())
//                .submittedAt(participantUserExamResult.getSubmittedAt())
                .exam(ParticipantUserExamResultViewDto.ExamViewDto.builder()
                        .id(participantUserExamResult.getExam().getId())
                        .description(participantUserExamResult.getExam().getDescription())
                        .totalMarks(participantUserExamResult.getExam().getTotalMarks())
                        .subjectName(participantUserExamResult.getExam().getSubject().getName())
                        .totalQuestions(participantUserExamResult.getExam().getQuestions().size())
//                        .totalEnrolledUsers(participantUserExamResult.getExam().getEnrolledParticipantUsers().size())
                        .build())
//                .answers(participantUserExamResult.getAnswers().stream()
//                        .map(userAnswer -> ParticipantUserExamResultViewDto.ParticipantUserAnswerViewDto.builder()
//                                .id(userAnswer.getId())
//                                .answerGiven(userAnswer.getAnswerGiven())
//                                .isCorrect(userAnswer.getIsCorrect())
//                                .question(ParticipantUserExamResultViewDto.ExamQuestionViewDto.builder()
//                                        .id(userAnswer.getQuestion().getId())
//                                        .text(userAnswer.getQuestion().getText())
//                                        .options(userAnswer.getQuestion().getOptions())
//                                        .correctAnswer(userAnswer.getQuestion().getCorrectAnswer())
//                                        .complexity(userAnswer.getQuestion().getComplexity())
//                                        .build())
//                                .build())
//                        .collect(Collectors.toList()))
                .analytics(ParticipantUserExamResultViewDto.ParticipantUserAnalyticsViewDto.builder()
//                        .id(participantUserExamResult.getAnalytics().getId())
//                        .attemptedQuestions(participantUserExamResult.getAnalytics().getAttemptedQuestions())
//                        .correctAnswers(participantUserExamResult.getAnalytics().getCorrectAnswers())
//                        .accuracy(participantUserExamResult.getAnalytics().getAccuracy())
                        .build())
                .build();
    }

}
