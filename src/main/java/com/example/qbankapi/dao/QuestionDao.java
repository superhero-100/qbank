package com.example.qbankapi.dao;

import com.example.qbankapi.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Question> findByText(String text) {
        List<Question> questionList = entityManager.createQuery("SELECT q FROM Question q WHERE q.text = :text", Question.class).setParameter("text", text).getResultList();
        return questionList.isEmpty() ? Optional.empty() : Optional.of(questionList.getFirst());
    }

    public void save(Question question) {
        entityManager.persist(question);
    }

//    public QuestionViewPageDto findFilteredQuestions(QuestionFilterDto filter) {
//        StringBuilder sql = new StringBuilder("SELECT q FROM Question q WHERE 1 = 1 AND q.isActive = true");
//        Map<String, Object> parameters = new HashMap<>();
//
//        if (filter.getSubjectId() > 0) {
//            sql.append(" AND q.subject.id = :subjectId");
//            parameters.put("subjectId", filter.getSubjectId());
//        }
//
//        if (!filter.getComplexity().equals("ALL")) {
//            sql.append(" AND q.complexity = :complexity");
//            parameters.put("complexity", Question.Complexity.valueOf(filter.getComplexity().toUpperCase()));
//        }
//
//        if (filter.getMarks() > 0) {
//            sql.append(" AND q.marks = :marks");
//            parameters.put("marks", filter.getMarks());
//        }
//
//        sql.append(" ORDER BY q.").append(filter.getSortBy()).append(" ").append(filter.getSortOrder()).append(", q.id ASC");
//
//        TypedQuery<Question> query = entityManager.createQuery(sql.toString(), Question.class);
//        parameters.forEach(query::setParameter);
//
//        query.setFirstResult(filter.getPage() * filter.getPageSize());
//        query.setMaxResults(filter.getPageSize() + 1);
//
//        List<Question> results = query.getResultList();
//
//        boolean hasNext = results.size() > filter.getPageSize();
//
//        if (hasNext) results.remove(results.size() - 1);
//
//        List<QuestionDto> questionDtoList = results.stream()
//                .map(question -> QuestionDto.builder()
//                        .id(question.getId())
//                        .text(question.getText())
//                        .options(question.getOptions())
//                        .correctAnswer(question.getCorrectAnswer())
//                        .complexity(question.getComplexity())
//                        .marks(question.getMarks())
//                        .subjectName(question.getSubject().getName())
//                        .build())
//                .toList();
//        QuestionViewPageDto questionViewPage = new QuestionViewPageDto();
//        questionViewPage.setQuestions(questionDtoList);
//        questionViewPage.setPage(filter.getPage());
//        questionViewPage.setPageSize(filter.getPageSize());
//        questionViewPage.setHasNextPage(hasNext);
//        return questionViewPage;
//    }
//
//    private final String RANDOM_QUESTIONS_QUERY_SQL = "SELECT * FROM question_tbl " +
//            "WHERE subject_id = :subjectId " +
//            "AND complexity = :complexity " +
//            "AND marks = :marks " +
//            "ORDER BY RAND() " +
//            "LIMIT :limit";
//
//    public List<Question> findRandomQuestions(Long subjectId, Integer totalQuestions, Question.Complexity complexity, Integer marks) {
//        return entityManager.createNativeQuery(RANDOM_QUESTIONS_QUERY_SQL, Question.class)
//                .setParameter("subjectId", subjectId)
//                .setParameter("complexity", complexity.toString())
//                .setParameter("marks", marks)
//                .setParameter("limit", totalQuestions)
//                .getResultList();
//    }
//
//    public Optional<Question> findById(Long id) {
//        List<Question> questionList = entityManager.createQuery("SELECT q FROM Question q WHERE q.id = :id", Question.class).setParameter("id", id).getResultList();
//        return questionList.isEmpty() ? Optional.empty() : Optional.of(questionList.getFirst());
//    }
//
//    public void update(Question question) {
//        entityManager.merge(question);
//    }


//    public List<Question> findAll() {
//        return entityManager.createQuery("SELECT q FROM Question  q", Question.class).getResultList();
//    }

//    public Optional<Question> getById(Long id) {
//        List<Question> questionList = entityManager.createQuery("SELECT q FROM Question q WHERE q.id = :id", Question.class).setParameter("id", id).getResultList();
//        return Optional.ofNullable(questionList.size() == 0 ? null : questionList.get(0));
//    }

}
