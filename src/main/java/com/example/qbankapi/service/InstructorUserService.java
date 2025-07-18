package com.example.qbankapi.service;

import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.view.InstructorUserProfileStatsViewDto;
import com.example.qbankapi.entity.*;
import com.example.qbankapi.exception.base.impl.InstructorUserNotFoundException;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import com.example.qbankapi.exception.base.impl.SubjectNotFoundException;
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
    private final SubjectDao subjectDao;

    @Transactional(readOnly = true)
    public InstructorUserProfileStatsViewDto getInstructorUserStats(Long instructorUserId,String currentUserZoneId) {
        InstructorUser instructorUser = instructorUserDao.findById(instructorUserId)
                .orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id [%d]", instructorUserId)));
        log.debug("Fetched Instructor user with id [{}]", instructorUserId);

        List<Exam> exams = instructorUser.getCreatedExams();
        List<Question> questions = instructorUser.getCreatedQuestions();
        List<Subject> subjects = instructorUser.getAssignedSubjects();

        return InstructorUserProfileStatsViewDto.builder()
                .id(instructorUser.getId())
                .username(instructorUser.getUsername())
                .email(instructorUser.getEmail())
                .zoneId(instructorUser.getZoneId())
                .registeredAt(instructorUser.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
//                ----
                .totalCreatedExams(exams.size())
                .totalCreatedQuestions(questions.size())
                .totalAssignedSubjects(subjects.size())
                .assignedSubjects(subjects.stream()
                        .map(subject -> InstructorUserProfileStatsViewDto.AssignedSubjectViewDto.builder()
                                .id(subject.getId())
                                .name(subject.getName())
                                .build())
                        .collect(Collectors.toList()))
                .recentExams(exams.stream()
                        .sorted(Comparator.comparing(Exam::getCreatedAt).reversed())
                        .limit(5)
                        .map(exam -> InstructorUserProfileStatsViewDto.RecentCreatedExamViewDto.builder()
                                .description(exam.getDescription())
                                .subjectName(exam.getSubject().getName())
                                .totalMarks(exam.getTotalMarks())
                                .createdAt(exam.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                                .build())
                        .collect(Collectors.toList()))
                .recentQuestions(questions.stream()
                        .sorted(Comparator.comparing(Question::getCreatedAt).reversed())
                        .limit(5)
                        .map(question -> InstructorUserProfileStatsViewDto.RecentCreatedQuestionsViewDto.builder()
                                .questionText(question.getText())
                                .subjectName(question.getSubject().getName())
                                .createdAt(question.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void assignSubject(Long instructorUserId, Long subjectId) {
        InstructorUser instructorUser = instructorUserDao.findById(instructorUserId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorUserId)));

        Subject subject = subjectDao.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", subjectId)));

        if (!instructorUser.getAssignedSubjects().contains(subject)) {
            instructorUser.getAssignedSubjects().add(subject);

            instructorUserDao.update(instructorUser);
            log.info("Subject with id [{}] assigned to instructor with id [{}]", subjectId, instructorUserId);
        } else {
            log.info("Subject with id [{}] already assigned to instructor with id [{}]", subjectId, instructorUserId);
        }
    }

    @Transactional
    public void revokeSubject(Long instructorUserId, Long subjectId) {
        InstructorUser instructorUser = instructorUserDao.findById(instructorUserId)
                .orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", instructorUserId)));

        Subject subject = subjectDao.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", subjectId)));

        if (instructorUser.getAssignedSubjects().contains(subject)) {
            instructorUser.getAssignedSubjects().remove(subject);

            instructorUserDao.update(instructorUser);
            log.info("Subject with id [{}] revoked from instructor with id [{}]", subjectId, instructorUserId);
        } else {
            log.warn("Attempted to revoke subject with id [{}] not assigned to instructor with id [{}]", subjectId, instructorUserId);
        }
    }

}
