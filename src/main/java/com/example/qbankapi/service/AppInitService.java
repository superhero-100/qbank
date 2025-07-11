package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppInitService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final SubjectDao subjectDao;
    private final BaseUserDao baseUserDao;
    private final QuestionDao questionDao;

    @Transactional
    public void createSubjectIfNotExists(String name, String description, ZonedDateTime createdAt, String zoneId) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription(description);
        subject.setQuestions(List.of());
        subject.setExams(List.of());
        subject.setAssignedInstructors(List.of());
        subject.setCreatedAt(createdAt);
        subject.setCreationZone(zoneId);

        subjectDao.findByName(subject.getName()).ifPresentOrElse(sub -> log.info("Subject with name: {} exists", sub.getName()), () -> {
            subjectDao.save(subject);
            log.info("Subject with name: {} created", subject.getName());
        });
    }

    @Transactional
    public void createAndSaveAdminUserIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setEmail(email);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setCreatedAt(createdAt);
        adminUser.setCreationZone(zoneId);
        adminUser.setStatus(status);
        adminUser.setZoneId(zoneId);
        adminUser.setCreatedQuestions(List.of());
        adminUser.setCreatedExams(List.of());

        baseUserDao.findByUsername(adminUser.getUsername()).ifPresentOrElse(baseUser -> log.info("Admin with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(adminUser);
            log.info("Admin with username: {} created", adminUser.getUsername());
        });
    }

    @Transactional
    public void createAndSaveInstructorIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        InstructorUser instructorUser = new InstructorUser();
        instructorUser.setUsername(username);
        instructorUser.setEmail(email);
        instructorUser.setPassword(passwordEncoder.encode(password));
        instructorUser.setCreatedAt(createdAt);
        instructorUser.setCreationZone(zoneId);
        instructorUser.setStatus(status);
        instructorUser.setZoneId(zoneId);
        instructorUser.setAssignedSubjects(List.of());
        instructorUser.setCreatedQuestions(List.of());
        instructorUser.setCreatedExams(List.of());

        baseUserDao.findByUsername(instructorUser.getUsername()).ifPresentOrElse(baseUser -> log.info("Teacher with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(instructorUser);
            log.info("Teacher with username: {} created", instructorUser.getUsername());
        });
    }

    @Transactional
    public void createAndSaveParticipantIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        ParticipantUser participantUser = new ParticipantUser();
        participantUser.setUsername(username);
        participantUser.setEmail(email);
        participantUser.setPassword(passwordEncoder.encode(password));
        participantUser.setCreatedAt(createdAt);
        participantUser.setCreationZone(zoneId);
        participantUser.setStatus(status);
        participantUser.setZoneId(zoneId);
        participantUser.setExamEnrollments(List.of());
        participantUser.setParticipantUserExamSubmissions(List.of());
        participantUser.setParticipantUserExamResults(List.of());

        baseUserDao.findByUsername(participantUser.getUsername()).ifPresentOrElse(baseUser -> log.info("Admin with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(participantUser);
            log.info("Admin with username: {} created", participantUser.getUsername());
        });
    }

    @Transactional
    public void createQuestionIfNotExists(String text, List<String> options, Question.Option correctAnswer, Question.Complexity complexity, Long marks, Long subjectId, String zoneId) {
        Subject subject = new Subject();
        subject.setId(subjectId);

        AdminUser adminUser = new AdminUser();
        adminUser.setId(1L);

        Question question = new Question();
        question.setText(text);
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);
        question.setComplexity(complexity);
        question.setMarks(marks);
        question.setIsActive(Boolean.TRUE);
        question.setSubject(subject);
        question.setAssociatedExams(List.of());
        question.setParticipantUserExamQuestionAnswers(List.of());
        question.setCreatedByBaseUser(adminUser);
        question.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        question.setCreationZone(zoneId);

        questionDao.findByText(question.getText()).ifPresentOrElse(que -> log.info("Question with id: {} exists", que), () -> {
            questionDao.save(question);
            log.info("Question with id: {} created", question.getId());
        });
    }

}
