package com.example.qbankapi.dao;

import com.example.qbankapi.dto.AddQuestionRequestDto;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAll(List<AddQuestionRequestDto> questionList) {
        questionList.stream().forEach(question -> {
            Subject subject = entityManager.find(Subject.class, question.getSubjectId());
            Optional.ofNullable(subject).orElseThrow(() -> new IllegalArgumentException("invalid data"));

            Question newQuestion = Question.builder()
                    .text(question.getText())
                    .correctAnswer(question.getCorrectAnswer())
                    .complexity(question.getComplexity())
                    .subject(subject)
                    .build();
            newQuestion.getOptions().add(question.getOptionA());
            newQuestion.getOptions().add(question.getOptionB());
            newQuestion.getOptions().add(question.getOptionC());
            newQuestion.getOptions().add(question.getOptionD());


            subject.getQuestions().add(newQuestion);

            entityManager.persist(subject);
            entityManager.persist(newQuestion);
        });
    }

    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return entityManager.createQuery("SELECT q FROM Question  q", Question.class).getResultList();
    }

}
