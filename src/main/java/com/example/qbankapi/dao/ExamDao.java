package com.example.qbankapi.dao;

import com.example.qbankapi.dto.model.AllExamFilterDto;
import com.example.qbankapi.dto.model.InstructorCreatedExamsFilterDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.dto.view.ExamViewDto;
import com.example.qbankapi.entity.Exam;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ExamDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ExamPageViewDto findFilteredExams(AllExamFilterDto filter) {
        StringBuilder sql = new StringBuilder("SELECT e FROM Exam e WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();

        if (filter.getSubjectId() > 0) {
            sql.append(" AND e.subject.id = :subjectId");
            parameters.put("subjectId", filter.getSubjectId());
        }

        sql.append(" ORDER BY e.").append(filter.getSortBy()).append(" ").append(filter.getSortOrder()).append(", e.id ASC");

        TypedQuery<Exam> query = entityManager.createQuery(sql.toString(), Exam.class);
        parameters.forEach(query::setParameter);

        query.setFirstResult(filter.getPage() * filter.getPageSize());
        query.setMaxResults(filter.getPageSize() + 1);

        List<Exam> results = query.getResultList();

        boolean hasNext = results.size() > filter.getPageSize();

        if (hasNext) results.remove(results.size() - 1);

        List<ExamViewDto> examDetailsDtoList = results.stream()
                .map(exam -> ExamViewDto.builder()
                        .id(exam.getId())
                        .description(exam.getDescription())
                        .subjectName(exam.getSubject().getName())
                        .totalMarks(exam.getTotalMarks())
                        .questionsCount(exam.getQuestions().size())
                        .enrollmentCount(exam.getParticipantEnrollments().size())
                        .build())
                .toList();
        ExamPageViewDto examPageViewDto = new ExamPageViewDto();
        examPageViewDto.setExams(examDetailsDtoList);
        examPageViewDto.setPage(filter.getPage());
        examPageViewDto.setPageSize(filter.getPageSize());
        examPageViewDto.setHasNextPage(hasNext);

        return examPageViewDto;
    }

    public void save(Exam exam) {
        entityManager.persist(exam);
    }

//    public List<Exam> findAllExams() {
//        return entityManager.createQuery("SELECT e FROM Exam e", Exam.class).getResultList();
//    }
//
//    public List<Exam> findAllExamsBySubjectId(Long subjectId) {
//        return entityManager.createQuery("SELECT e FROM Exam e WHERE e.subject.id = :subjectId", Exam.class).setParameter("subjectId", subjectId).getResultList();
//    }

//    public List<Exam> findAllByEnrollmentEndDate(ZonedDateTime nowUtc, User user) {
//        return entityManager.createQuery("SELECT e FROM Exam e WHERE e.enrollmentStartDate < :nowUtc AND e.enrollmentEndDate > :nowUtc AND :user NOT MEMBER OF e.enrolledUsers", Exam.class).setParameter("nowUtc", nowUtc).setParameter("user", user).getResultList();
//    }
//
//    public List<Exam> findAllByEnrollmentEndDateAndSubjectId(ZonedDateTime nowUtc, Long subjectId, User user) {
//        return entityManager.createQuery("SELECT e FROM Exam e WHERE e.enrollmentStartDate < :nowUtc AND e.enrollmentEndDate > :nowUtc AND e.subject.id = :subjectId AND :user NOT MEMBER OF e.enrolledUsers", Exam.class).setParameter("nowUtc", nowUtc).setParameter("subjectId", subjectId).setParameter("user", user).getResultList();
//    }

    public Optional<Exam> findById(Long id) {
        List<Exam> examList = entityManager.createQuery("SELECT e FROM Exam e WHERE e.id = :id", Exam.class).setParameter("id", id).getResultList();
        return examList.isEmpty() ? Optional.empty() : Optional.of(examList.getFirst());
    }

    public ExamPageViewDto findFilteredInstructorCreatedExams(InstructorCreatedExamsFilterDto filter, Long instructorId) {
        StringBuilder sql = new StringBuilder("SELECT e FROM Exam e WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();

        sql.append(" AND e.createdByBaseUser.id = :id");
        parameters.put("id", instructorId);

        if (filter.getSubjectId() > 0) {
            sql.append(" AND e.subject.id = :subjectId");
            parameters.put("subjectId", filter.getSubjectId());
        }

        sql.append(" ORDER BY e.").append(filter.getSortBy()).append(" ").append(filter.getSortOrder()).append(", e.id ASC");

        TypedQuery<Exam> query = entityManager.createQuery(sql.toString(), Exam.class);
        parameters.forEach(query::setParameter);

        query.setFirstResult(filter.getPage() * filter.getPageSize());
        query.setMaxResults(filter.getPageSize() + 1);

        List<Exam> results = query.getResultList();

        boolean hasNext = results.size() > filter.getPageSize();

        if (hasNext) results.removeLast();

        List<ExamViewDto> examDetailsDtoList = results.stream()
                .map(exam -> ExamViewDto.builder()
                        .id(exam.getId())
                        .description(exam.getDescription())
                        .subjectName(exam.getSubject().getName())
                        .totalMarks(exam.getTotalMarks())
                        .questionsCount(exam.getQuestions().size())
                        .enrollmentCount(exam.getParticipantEnrollments().size())
                        .build())
                .toList();
        ExamPageViewDto examPageViewDto = new ExamPageViewDto();
        examPageViewDto.setExams(examDetailsDtoList);
        examPageViewDto.setPage(filter.getPage());
        examPageViewDto.setPageSize(filter.getPageSize());
        examPageViewDto.setHasNextPage(hasNext);

        return examPageViewDto;
    }

//    public void update(Exam exam) {
//        entityManager.merge(exam);
//    }

}
