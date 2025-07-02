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

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SubjectDao subjectDao;
    private final BaseUserDao baseUserDao;
    private final QuestionDao questionDao;

    @Transactional
    public void createSubjectIfNotExists(String name, String description) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription(description);
        subject.setQuestions(List.of());
        subject.setExams(List.of());

        subjectDao.findByName(subject.getName()).ifPresentOrElse(sub -> log.info("Subject with name: {} exists", sub.getName()), () -> {
            subjectDao.save(subject);
            log.info("Subject with name: {} created", subject.getName());
        });
    }

    @Transactional
    public void createAndSaveAdminIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(bCryptPasswordEncoder.encode(password));
        admin.setCreatedAt(createdAt);
        admin.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        admin.setStatus(status);
        admin.setZoneId(zoneId);

        baseUserDao.findByUsername(admin.getUsername()).ifPresentOrElse(baseUser -> log.info("Admin with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(admin);
            log.info("Admin with username: {} created", admin.getUsername());
        });
    }

    @Transactional
    public void createAndSaveTeacherIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setEmail(email);
        teacher.setPassword(bCryptPasswordEncoder.encode(password));
        teacher.setCreatedAt(createdAt);
        teacher.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        teacher.setStatus(status);
        teacher.setZoneId(zoneId);

        baseUserDao.findByUsername(teacher.getUsername()).ifPresentOrElse(baseUser -> log.info("Teacher with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(teacher);
            log.info("Teacher with username: {} created", teacher.getUsername());
        });
    }

    @Transactional
    public void createAndSaveUserIfNotExists(String username, String email, String password, ZonedDateTime createdAt, BaseUser.Status status, String zoneId) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCreatedAt(createdAt);
        user.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        user.setEnrolledExams(List.of());
        user.setCompletedExams(List.of());
        user.setUserExamResults(List.of());
        user.setStatus(status);
        user.setZoneId(zoneId);

        baseUserDao.findByUsername(user.getUsername()).ifPresentOrElse(baseUser -> log.info("Admin with username: {} exists", baseUser.getUsername()), () -> {
            baseUserDao.save(user);
            log.info("Admin with username: {} created", user.getUsername());
        });
    }

    @Transactional
    public void createQuestionIfNotExists(String text, List<String> options, Question.Option correctAnswer, Question.Complexity complexity, Long marks, Long subjectId) {
        Subject subject = new Subject();
        subject.setId(subjectId);

        Question question = new Question();
        question.setText(text);
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);
        question.setComplexity(complexity);
        question.setMarks(marks);
        question.setSubject(subject);

        questionDao.findByText(question.getText()).ifPresentOrElse(que -> log.info("Question with id: {} exists", que), () -> {
            questionDao.save(question);
            log.info("Question with id: {} created", question.getId());
        });
    }

}
