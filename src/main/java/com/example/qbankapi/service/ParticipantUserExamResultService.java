package com.example.qbankapi.service;

import com.example.qbankapi.dao.ParticipantUserExamEnrollmentDao;
import com.example.qbankapi.dao.ParticipantUserExamResultDao;
import com.example.qbankapi.dto.view.ParticipantUserExamResultViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.AccessDeniedException;
import com.example.qbankapi.exception.base.impl.ParticipantUserExamEnrollmentNotFoundException;
import com.example.qbankapi.exception.base.impl.ParticipantUserExamResultNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantUserExamResultService {

    private final ParticipantUserExamEnrollmentDao participantUserExamEnrollmentDao;

    @Transactional
    public ParticipantUserExamResultViewDto getParticipantUserExamResultInDtoByExamEnrollmentId(Long examEnrollmentId, Long currentUserId) {
        ParticipantUserExamEnrollment participantUserExamEnrollment = participantUserExamEnrollmentDao.findById(examEnrollmentId).orElseThrow(() -> new ParticipantUserExamEnrollmentNotFoundException(String.format("ParticipantUserExamEnrollment not found with ID [%d]", examEnrollmentId)));

        boolean isExamSubmitted = participantUserExamEnrollment.getExamAttemptStatus().equals(ParticipantUserExamEnrollment.ExamAttemptStatus.SUBMITTED);

        ParticipantUser participantUser = participantUserExamEnrollment.getParticipantUser();

        if (!participantUser.getId().equals(currentUserId)) {
            throw new AccessDeniedException("User is not authorized to view this exam result.");
        }

        Exam exam = participantUserExamEnrollment.getExam();

        if (ZonedDateTime.now(ZoneOffset.UTC).isBefore(exam.getExamEndDate())){
            throw new AccessDeniedException("Exam is not ended yet");
        }

        ParticipantUserExamResult participantUserExamResult = isExamSubmitted ? participantUserExamEnrollment.getParticipantUserExamSubmission().getParticipantUserExamResult() : null;
        ParticipantUserExamSubmission participantUserExamSubmission = isExamSubmitted ? participantUserExamResult.getParticipantUserExamSubmission() : null;
        ParticipantUserExamAnalytics participantUserExamAnalytics = isExamSubmitted ? participantUserExamResult.getParticipantUserExamAnalytics() : null;
        List<ParticipantUserExamQuestionAnswer> participantUserExamQuestionAnswers = isExamSubmitted ? participantUserExamSubmission.getParticipantUserExamQuestionAnswers() : null;

        return ParticipantUserExamResultViewDto.builder()
                .username(participantUser.getUsername())
                .isExamSubmitted(isExamSubmitted)
                .totalScore(isExamSubmitted ? participantUserExamResult.getTotalScore() : null)
                .submittedAt(isExamSubmitted ? participantUserExamSubmission.getCreatedAt().withZoneSameInstant(ZoneId.of(participantUser.getZoneId())) : null)
                .exam(ParticipantUserExamResultViewDto.ExamViewDto.builder()
                        .id(exam.getId())
                        .description(exam.getDescription())
                        .totalMarks(exam.getTotalMarks())
                        .subjectName(exam.getSubject().getName())
                        .questionsCount(exam.getQuestions().size())
                        .enrollmentsCount(exam.getParticipantEnrollments().size())
                        .build())
                .answers(isExamSubmitted ? participantUserExamQuestionAnswers.stream()
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
                        .collect(Collectors.toList()) : exam.getQuestions().stream()
                        .map(question -> ParticipantUserExamResultViewDto.ParticipantUserAnswerViewDto.builder()
                                .id(null)
                                .answerGiven(null)
                                .isCorrect(false)
                                .question(ParticipantUserExamResultViewDto.ExamQuestionViewDto.builder()
                                        .id(question.getId())
                                        .text(question.getText())
                                        .options(question.getOptions())
                                        .correctAnswer(question.getCorrectAnswer())
                                        .complexity(question.getComplexity())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .analytics(isExamSubmitted ? ParticipantUserExamResultViewDto.ParticipantUserAnalyticsViewDto.builder()
                        .id(participantUserExamAnalytics.getId())
                        .attemptedQuestions(participantUserExamAnalytics.getAttemptedQuestions())
                        .correctAnswers(participantUserExamAnalytics.getCorrectAnswers())
                        .accuracy(participantUserExamAnalytics.getAccuracy())
                        .build() : null)
                .build();
    }

}
