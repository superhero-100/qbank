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

//    private final String RANDOM_QUESTIONS_QUERY_SQL = "SELECT * FROM question_tbl " +
//            "WHERE subject_id = :subjectId " +
//            "AND complexity = :complexity " +
//            "ORDER BY RAND() " +
//            "LIMIT :limit";
//
//
//    public void save(Question question) {
//        entityManager.persist(question);
//    }
//
//    public List<Question> findAll() {
//        return entityManager.createQuery("SELECT q FROM Question  q", Question.class).getResultList();
//    }
//
//    public List<Question> findRandomQuestions(Long subjectId, Integer totalQuestions, Question.Complexity complexity) {
//        return entityManager.createNativeQuery(RANDOM_QUESTIONS_QUERY_SQL, Question.class)
//                .setParameter("subjectId", subjectId)
//                .setParameter("limit", totalQuestions)
//                .setParameter("complexity", complexity.toString())
//                .getResultList();
//    }
//
//    public Optional<Question> getById(Long id) {
//        List<Question> questionList = entityManager.createQuery("SELECT q FROM Question q WHERE q.id = :id", Question.class).setParameter("id", id).getResultList();
//        return Optional.ofNullable(questionList.size() == 0 ? null : questionList.get(0));
//    }

}
