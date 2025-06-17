package com.example.qbankapi.dao;

import com.example.qbankapi.dto.CreateExamRequestDto;
import com.example.qbankapi.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Optional;

@Repository
public class ExamDao {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void save(CreateExamRequestDto examDTO) {
        Subject subject = entityManager.find(Subject.class, examDTO.getSubjectId());
        Optional.ofNullable(subject)
                .orElseThrow(() -> new IllegalArgumentException("invalid data"));

        ExamAnalytics analytics = ExamAnalytics.builder().build();

        Integer totalMarks = (examDTO.getTotalEasyQuestions() * Question.Complexity.EASY.getMarks()) + (examDTO.getTotalMediumQuestions() * Question.Complexity.MEDIUM.getMarks()) + (examDTO.getTotalHardQuestions() * Question.Complexity.HARD.getMarks());
        Exam exam = Exam.builder()
                .description(examDTO.getDescription())
                .totalMarks(totalMarks)
                .subject(subject)
                .analytics(analytics)
                .build();

        String sql = "SELECT * FROM question " +
                "WHERE subject_id = :subjectId " +
                "AND complexity = :complexity" +
                "ORDER BY RAND() " +
                "LIMIT :limit";

        exam.getQuestions()
                .addAll(entityManager.createNativeQuery(sql, Question.class)
                .setParameter("subjectId", subject.getId())
                .setParameter("limit", examDTO.getTotalEasyQuestions())
                .setParameter("complexity", Question.Complexity.EASY.toString())
                .getResultList());
        exam.getQuestions()
                .addAll(entityManager.createNativeQuery(sql, Question.class)
                .setParameter("subjectId", subject.getId())
                .setParameter("limit", examDTO.getTotalMediumQuestions())
                .setParameter("complexity", Question.Complexity.MEDIUM.toString())
                .getResultList());
        exam.getQuestions()
                .addAll(entityManager.createNativeQuery(sql, Question.class)
                .setParameter("subjectId", subject.getId())
                .setParameter("limit", examDTO.getTotalHardQuestions())
                .setParameter("complexity", Question.Complexity.HARD.toString())
                .getResultList());

        subject.getExams().add(exam);
        analytics.setExam(exam);

        entityManager.persist(subject);
        entityManager.persist(analytics);
        entityManager.persist(exam);
    }

}
