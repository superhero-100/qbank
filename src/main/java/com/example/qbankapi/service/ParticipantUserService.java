package com.example.qbankapi.service;

import com.example.qbankapi.dao.*;

import com.example.qbankapi.dto.view.ExamViewDto;
import com.example.qbankapi.dto.view.ParticipantUserExamEnrollmentViewDto;
import com.example.qbankapi.dto.view.ParticipantUserProfileStatsViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantUserService {

    private final ParticipantUserDao participantUserDao;
    private final ParticipantUserExamEnrollmentDao participantUserExamEnrollmentDao;

    @Transactional(readOnly = true)
    public ParticipantUserProfileStatsViewDto getParticipantUserStats(Long participantUserId, String currentUserZoneId) {
        ParticipantUser participantUser = participantUserDao.findById(participantUserId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id [%d]", participantUserId)));

        List<ParticipantUserExamResult> participantUserExamResults = participantUser.getParticipantUserExamResults();
        List<ParticipantUserExamSubmission> participantUserExamSubmissions = participantUser.getParticipantUserExamSubmissions();

        ParticipantUserProfileStatsViewDto participantUserProfileStatsViewDto = ParticipantUserProfileStatsViewDto.builder()
                .username(participantUser.getUsername())
                .email(participantUser.getEmail())
                .zoneId(participantUser.getZoneId())
                .registeredAt(participantUser.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                .totalExamsEnrolled(participantUserExamResults.size())
                .totalExamsTaken(participantUserExamSubmissions.size())
                .bestScore(participantUserExamResults.stream()
                        .mapToInt(ParticipantUserExamResult::getTotalScore)
                        .max()
                        .orElse(0))
                .worstScore(participantUserExamResults.stream()
                        .mapToInt(ParticipantUserExamResult::getTotalScore)
                        .min()
                        .orElse(0))
                .averageScore(participantUserExamResults.stream()
                        .mapToInt(ParticipantUserExamResult::getTotalScore)
                        .average()
                        .orElse(0.0))
                .totalQuestionsAttempted(participantUserExamResults.stream()
                        .map(ParticipantUserExamResult::getParticipantUserExamAnalytics)
                        .mapToInt(ParticipantUserExamAnalytics::getAttemptedQuestions)
                        .sum())
                .totalCorrectAnswers(participantUserExamResults.stream()
                        .map(ParticipantUserExamResult::getParticipantUserExamAnalytics)
                        .mapToInt(ParticipantUserExamAnalytics::getCorrectAnswers)
                        .sum())
                .averageAccuracy(participantUserExamResults.stream()
                        .map(ParticipantUserExamResult::getParticipantUserExamAnalytics)
                        .mapToDouble(ParticipantUserExamAnalytics::getAccuracy)
                        .average().orElse(0.0))
                .lastExamTakenAt(participantUserExamSubmissions.isEmpty() ? null : participantUserExamSubmissions.getLast().getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                .recentExams(participantUserExamResults.stream()
                        .sorted(Comparator.comparing(
                                (ParticipantUserExamResult result) -> result.getParticipantUserExamSubmission().getCreatedAt()
                        ).reversed())
                        .limit(5)
                        .map(result -> {
                            Exam exam = result.getExam();
                            ParticipantUserExamSubmission participantUserExamSubmission = result.getParticipantUserExamSubmission();
                            return ParticipantUserProfileStatsViewDto.RecentExamViewDto.builder()
                                    .examDescription(exam.getDescription())
                                    .subjectName(exam.getSubject().getName())
                                    .score(result.getTotalScore())
                                    .submittedAt(participantUserExamSubmission.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                                    .accuracy(result.getParticipantUserExamAnalytics().getAccuracy())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
        return participantUserProfileStatsViewDto;
    }


    @Transactional(readOnly = true)
    public ParticipantUser findById(Long id) {
        return participantUserDao.findById(id).orElseThrow(() -> new ParticipantUserNotFoundException("Participant not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ParticipantUserExamEnrollmentViewDto> getAllEnrolledExamDtoList(Long participantUserId) {
        ParticipantUser participantUser = participantUserDao.findById(participantUserId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant User not found with id: %d", participantUserId)));

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        List<ParticipantUserExamEnrollment> participantUserExamEnrollments = participantUserExamEnrollmentDao.findAllByParticipantUserIdAndNowIsAfterExamEnd(participantUserId, nowUtc);

        return participantUserExamEnrollments.stream().map(userExamEnrollment -> {
            Exam exam = userExamEnrollment.getExam();
            Subject subject = exam.getSubject();
            return ParticipantUserExamEnrollmentViewDto.builder()
                    .id(userExamEnrollment.getId())
                    .exam(ExamViewDto.builder()
                            .description(exam.getDescription())
                            .totalMarks(exam.getTotalMarks())
                            .subjectName(subject.getName())
                            .questionsCount(exam.getQuestions().size())
                            .enrollmentCount(exam.getParticipantEnrollments().size())
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

//    @Transactional
//    public void addAll(List<AddUserRequestDto> userRequestDtoList) {
//        userRequestDtoList.stream().forEach(
//                user -> userDao.save(
//                        User.builder()
//                                .username(user.getUsername())
//                                .password(user.getPassword())
//                                .build()
//                )
//        );
//    }
//
//    @Transactional(readOnly = true)
//    public List<User> getAll() {
//        return userDao.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<User> getByUsername(String username) {
//        return userDao.findByUsername(username);
//    }

}
