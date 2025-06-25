package com.example.qbankapi.service;

import com.example.qbankapi.dao.UserDao;
import com.example.qbankapi.dto.AddUserRequestDto;
import com.example.qbankapi.dto.UserEnrolledExamDetailsDto;
import com.example.qbankapi.dto.UserProfileStatsDto;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.entity.UserAnalytics;
import com.example.qbankapi.entity.UserExamResult;
import com.example.qbankapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

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
//
//    @Transactional(readOnly = true)
//    public User findById(Long userId) {
//        return userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException());
//    }
//
//    @Transactional(readOnly = true)
//    public List<UserEnrolledExamDetailsDto> getEnrolledExamDtos(Long id) {
//        User user = userDao.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
//        return user.getUserExamResults().stream()
//                .map(userExamResult -> UserEnrolledExamDetailsDto.builder()
//                        .id(userExamResult.getExam().getId())
//                        .description(userExamResult.getExam().getDescription())
//                        .totalMarks(userExamResult.getExam().getTotalMarks())
//                        .subjectName(userExamResult.getExam().getSubject().getName())
//                        .totalQuestions(userExamResult.getExam().getQuestions().size())
//                        .totalEnrolledUsers(userExamResult.getExam().getEnrolledUsers().size())
//                        .resultId(userExamResult.getId())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public UserProfileStatsDto getUserStats(Long userId) {
//        User user = userDao.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
//
//        List<UserExamResult> results = user.getUserExamResults();
//
//        Integer totalExams = results.size();
//        Double avgScore = results.stream()
//                .mapToInt(UserExamResult::getTotalScore)
//                .average().orElse(0.0);
//
//        Integer totalAttempted = results.stream()
//                .map(UserExamResult::getAnalytics)
//                .mapToInt(UserAnalytics::getAttemptedQuestions)
//                .sum();
//
//        Integer totalCorrect = results.stream()
//                .map(UserExamResult::getAnalytics)
//                .mapToInt(UserAnalytics::getCorrectAnswers)
//                .sum();
//
//        Double avgAccuracy = results.stream()
//                .map(UserExamResult::getAnalytics)
//                .mapToDouble(UserAnalytics::getAccuracy)
//                .average().orElse(0.0);
//
//        List<UserProfileStatsDto.RecentExamDto> recent = results.stream()
//                .sorted(Comparator.comparing(UserExamResult::getSubmittedAt).reversed())
//                .limit(5)
//                .map(r -> UserProfileStatsDto.RecentExamDto.builder()
//                        .examDescription(r.getExam().getDescription())
//                        .subjectName(r.getExam().getSubject().getName())
//                        .score(r.getTotalScore())
//                        .submittedAt(Date.from(r.getSubmittedAt().atZone(ZoneId.systemDefault()).toInstant()))
//                        .accuracy(r.getAnalytics().getAccuracy())
//                        .build())
//                .collect(Collectors.toList());
//
//        return UserProfileStatsDto.builder()
//                .username(user.getUsername())
//                .totalExamsTaken(totalExams)
//                .averageScore(avgScore)
//                .totalQuestionsAttempted(totalAttempted)
//                .totalCorrectAnswers(totalCorrect)
//                .averageAccuracy(avgAccuracy)
//                .recentExams(recent)
//                .build();
//    }

}
