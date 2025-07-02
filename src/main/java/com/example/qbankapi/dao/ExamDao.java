package com.example.qbankapi.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Repository
public class ExamDao {

    @PersistenceContext
    private EntityManager entityManager;

//    public void save(Exam exam) {
//        entityManager.persist(exam);
//    }
//
//    public List<Exam> findAllByEnrollmentEndDate(ZonedDateTime nowUtc, User user) {
//        return entityManager.createQuery("SELECT e FROM Exam e WHERE e.enrollmentStartDate < :nowUtc AND e.enrollmentEndDate > :nowUtc AND :user NOT MEMBER OF e.enrolledUsers", Exam.class).setParameter("nowUtc", nowUtc).setParameter("user", user).getResultList();
//    }
//
//    public List<Exam> findAllByEnrollmentEndDateAndSubjectId(ZonedDateTime nowUtc, Long subjectId, User user) {
//        return entityManager.createQuery("SELECT e FROM Exam e WHERE e.enrollmentStartDate < :nowUtc AND e.enrollmentEndDate > :nowUtc AND e.subject.id = :subjectId AND :user NOT MEMBER OF e.enrolledUsers", Exam.class).setParameter("nowUtc", nowUtc).setParameter("subjectId", subjectId).setParameter("user", user).getResultList();
//    }
//
//    public ExamViewPageDto findFilteredExams(ExamFilterDto filter) {
//        StringBuilder sql = new StringBuilder("SELECT e FROM Exam e WHERE 1=1");
//        Map<String, Object> parameters = new HashMap<>();
//
//        if (filter.getSubjectId() > 0) {
//            sql.append(" AND e.subject.id = :subjectId");
//            parameters.put("subjectId", filter.getSubjectId());
//        }
//
//        sql.append(" ORDER BY e.").append(filter.getSortBy()).append(" ").append(filter.getSortOrder()).append(", e.id ASC");
//
//        TypedQuery<Exam> query = entityManager.createQuery(sql.toString(), Exam.class);
//        parameters.forEach(query::setParameter);
//
//        query.setFirstResult(filter.getPage() * filter.getPageSize());
//        query.setMaxResults(filter.getPageSize() + 1);
//
//        List<Exam> results = query.getResultList();
//
//        boolean hasNext = results.size() > filter.getPageSize();
//
//        if (hasNext) results.remove(results.size() - 1);
//
//        List<ExamDetailsDto> examDetailsDtoList = results.stream()
//                .map(exam -> ExamDetailsDto.builder()
//                        .id(exam.getId())
//                        .description(exam.getDescription())
//                        .subjectName(exam.getSubject().getName())
//                        .totalMarks(exam.getTotalMarks())
//                        .totalQuestions(exam.getQuestions().size())
//                        .totalEnrolledUsers(exam.getEnrolledUsers().size())
//                        .createdAt(exam.getCreatedAt())
//                        .build())
//                .toList();
//        ExamViewPageDto questionViewPage = new ExamViewPageDto();
//        questionViewPage.setExams(examDetailsDtoList);
//        questionViewPage.setPage(filter.getPage());
//        questionViewPage.setPageSize(filter.getPageSize());
//        questionViewPage.setHasNextPage(hasNext);
//        return questionViewPage;
//    }
//
//    public Optional<Exam> findById(Long id) {
//        List<Exam> examList = entityManager.createQuery("SELECT e FROM Exam e WHERE e.id = :id", Exam.class).setParameter("id", id).getResultList();
//        return examList.isEmpty() ? Optional.empty() : Optional.of(examList.getFirst());
//    }
//
//    public void update(Exam exam) {
//        entityManager.merge(exam);
//    }

}
