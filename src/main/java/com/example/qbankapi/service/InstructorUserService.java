package com.example.qbankapi.service;

import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dto.view.InstructorUserProfileStatsViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import lombok.RequiredArgsConstructor;
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
public class InstructorUserService {

    private final InstructorUserDao instructorUserDao;

    @Transactional(readOnly = true)
    public InstructorUserProfileStatsViewDto getInstructorUserStats(Long userId, String adminZoneId) {
        InstructorUser instructorUser = instructorUserDao.findById(userId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id: %d", userId)));

        ZoneId zoneId = ZoneId.of(adminZoneId);

        List<Exam> exams = instructorUser.getCreatedExams();
        List<Question> questions = instructorUser.getCreatedQuestions();
        List<Subject> subjects = instructorUser.getAssignedSubjects();

        return InstructorUserProfileStatsViewDto.builder()
                .username(instructorUser.getUsername())
                .email(instructorUser.getEmail())
                .zoneId(instructorUser.getZoneId())
                .registeredAt(instructorUser.getCreatedAt().withZoneSameInstant(zoneId))
                .totalCreatedExams(exams.size())
                .totalCreatedQuestions(questions.size())
                .totalAssignedSubjects(subjects.size())
                .assignedSubjects(subjects.stream().map(Subject::getName).collect(Collectors.toList()))
                .recentExams(exams.stream()
                        .sorted(Comparator.comparing(Exam::getCreatedAt).reversed())
                        .limit(5)
                        .map(exam -> InstructorUserProfileStatsViewDto.RecentCreatedExamViewDto.builder()
                                .description(exam.getDescription())
                                .subjectName(exam.getSubject().getName())
                                .totalMarks(exam.getTotalMarks())
                                .createdAt(exam.getCreatedAt().withZoneSameInstant(zoneId))
                                .build())
                        .collect(Collectors.toList())
                )
                .recentQuestions(questions.stream()
                        .sorted(Comparator.comparing(Question::getCreatedAt).reversed())
                        .limit(5)
                        .map(question -> InstructorUserProfileStatsViewDto.RecentCreatedQuestionsViewDto.builder()
                                .questionText(question.getText())
                                .subjectName(question.getSubject().getName())
                                .createdAt(question.getCreatedAt().withZoneSameInstant(zoneId))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
