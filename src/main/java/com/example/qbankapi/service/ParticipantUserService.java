package com.example.qbankapi.service;

import com.example.qbankapi.dao.ParticipantUserDao;
import com.example.qbankapi.dto.view.ParticipantUserEnrolledExamDetailsViewDto;
import com.example.qbankapi.dto.view.ParticipantUserProfileStatsViewDto;
import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.entity.ParticipantUserExamAnalytics;
import com.example.qbankapi.entity.ParticipantUserExamResult;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantUserService {

    private final ParticipantUserDao participantUserDao;

    @Transactional(readOnly = true)
    public ParticipantUser findById(Long id) {
        return participantUserDao.findById(id).orElseThrow(() -> new ParticipantUserNotFoundException("Participant not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ParticipantUserEnrolledExamDetailsViewDto> getAllEnrolledExamDtoList(Long id) {
        ParticipantUser participantUser = participantUserDao.findById(id).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant User not found with id: %d",id)));
        return participantUser.getParticipantUserExamResults().stream()
                .map(userExamResult -> ParticipantUserEnrolledExamDetailsViewDto.builder()
                        .id(userExamResult.getExam().getId())
                        .description(userExamResult.getExam().getDescription())
                        .totalMarks(userExamResult.getExam().getTotalMarks())
                        .subjectName(userExamResult.getExam().getSubject().getName())
                        .totalQuestions(userExamResult.getExam().getQuestions().size())
                        .totalEnrolledUsers(userExamResult.getExam().getEnrolledParticipantUsers().size())
                        .resultId(userExamResult.getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParticipantUserProfileStatsViewDto getParticipantUserStats(Long participantUserId) {
        ParticipantUser participantUser = participantUserDao.findById(participantUserId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id: %d", participantUserId)));

        List<ParticipantUserExamResult> results = participantUser.getParticipantUserExamResults();

        int totalExams = results.size();
        double avgScore = results.stream()
                .mapToInt(ParticipantUserExamResult::getTotalScore)
                .average().orElse(0.0);

        int totalAttempted = results.stream()
                .map(ParticipantUserExamResult::getAnalytics)
                .mapToInt(ParticipantUserExamAnalytics::getAttemptedQuestions)
                .sum();

        int totalCorrect = results.stream()
                .map(ParticipantUserExamResult::getAnalytics)
                .mapToInt(ParticipantUserExamAnalytics::getCorrectAnswers)
                .sum();

        double avgAccuracy = results.stream()
                .map(ParticipantUserExamResult::getAnalytics)
                .mapToDouble(ParticipantUserExamAnalytics::getAccuracy)
                .average().orElse(0.0);

        List<ParticipantUserProfileStatsViewDto.RecentExamViewDto> recent = results.stream()
                .sorted(Comparator.comparing(ParticipantUserExamResult::getSubmittedAt).reversed())
                .limit(5)
                .map(r -> ParticipantUserProfileStatsViewDto.RecentExamViewDto.builder()
                        .examDescription(r.getExam().getDescription())
                        .subjectName(r.getExam().getSubject().getName())
                        .score(r.getTotalScore())
                        .submittedAt(Date.from(r.getSubmittedAt().atZone(ZoneId.systemDefault()).toInstant()))
                        .accuracy(r.getAnalytics().getAccuracy())
                        .build())
                .collect(Collectors.toList());

        return ParticipantUserProfileStatsViewDto.builder()
                .username(participantUser.getUsername())
                .totalExamsTaken(totalExams)
                .averageScore(avgScore)
                .totalQuestionsAttempted(totalAttempted)
                .totalCorrectAnswers(totalCorrect)
                .averageAccuracy(avgAccuracy)
                .recentExams(recent)
                .build();
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
