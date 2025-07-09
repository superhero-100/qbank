package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminUserDao;
import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dao.ParticipantUserDao;

import com.example.qbankapi.dto.view.ParticipantUserProfileStatsViewDto;
import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.entity.ParticipantUserExamAnalytics;
import com.example.qbankapi.entity.ParticipantUserExamResult;
import com.example.qbankapi.entity.ParticipantUserExamSubmission;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantUserService {

    private final ParticipantUserDao participantUserDao;

    @Transactional(readOnly = true)
    public ParticipantUserProfileStatsViewDto getParticipantUserStats(Long userId, String currentUserZoneId) {
        ParticipantUser participantUser = participantUserDao.findById(userId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id: %d", userId)));

        ZoneId zoneId = ZoneId.of(currentUserZoneId);

        List<ParticipantUserExamResult> participantUserExamResults = participantUser.getParticipantUserExamResults();
        List<ParticipantUserExamSubmission> participantUserExamSubmissions = participantUser.getParticipantUserExamSubmissions();

        return ParticipantUserProfileStatsViewDto.builder()
                .username(participantUser.getUsername())
                .email(participantUser.getEmail())
                .zoneId(participantUser.getZoneId())
                .registeredAt(participantUser.getCreatedAt().withZoneSameInstant(zoneId))
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
                .lastExamTakenAt(participantUserExamSubmissions.isEmpty() ? null : participantUserExamSubmissions.getLast()
                        .getSubmittedAt()
                        .withZoneSameInstant(zoneId))
                .recentExams(participantUserExamResults.stream()
                        .sorted(Comparator.comparing(
                                (ParticipantUserExamResult result) -> result.getParticipantUserExamSubmission().getSubmittedAt()
                        ).reversed())
                        .limit(5)
                        .map(result -> ParticipantUserProfileStatsViewDto.RecentExamViewDto.builder()
                                .examDescription(result.getExam().getDescription())
                                .subjectName(result.getExam().getSubject().getName())
                                .score(result.getTotalScore())
                                .submittedAt(result.getParticipantUserExamSubmission().getSubmittedAt().withZoneSameInstant(zoneId))
                                .accuracy(result.getParticipantUserExamAnalytics().getAccuracy())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


//    @Transactional(readOnly = true)
//    public ParticipantUser findById(Long id) {
//        return participantUserDao.findById(id).orElseThrow(() -> new ParticipantUserNotFoundException("Participant not found with ID: " + id));
//    }
//
//    @Transactional(readOnly = true)
//    public List<ParticipantUserEnrolledExamDetailsViewDto> getAllEnrolledExamDtoList(Long id) {
//        ParticipantUser participantUser = participantUserDao.findById(id).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant User not found with id: %d",id)));
//        return participantUser.getParticipantUserExamResults().stream()
//                .map(userExamResult -> ParticipantUserEnrolledExamDetailsViewDto.builder()
//                        .id(userExamResult.getExam().getId())
//                        .description(userExamResult.getExam().getDescription())
//                        .totalMarks(userExamResult.getExam().getTotalMarks())
//                        .subjectName(userExamResult.getExam().getSubject().getName())
//                        .totalQuestions(userExamResult.getExam().getQuestions().size())
//                        .totalEnrolledUsers(userExamResult.getExam().getEnrolledParticipantUsers().size())
//                        .resultId(userExamResult.getId())
//                        .build())
//                .collect(Collectors.toList());
//    }

//    ---

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
