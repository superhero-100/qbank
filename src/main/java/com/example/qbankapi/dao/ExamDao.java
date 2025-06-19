package com.example.qbankapi.dao;

import com.example.qbankapi.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class ExamDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Exam exam) {
        entityManager.persist(exam);
    }

    public List<Exam> findAll() {
        return entityManager.createQuery("SELECT e FROM Exam e", Exam.class).getResultList();
    }

    public Optional<Exam> findById(Long id) {
        List<Exam> examList = entityManager.createQuery("SELECT e FROM Exam e WHERE e.id = :id", Exam.class).setParameter("id", id).getResultList();
        return Optional.ofNullable(examList.size() == 0 ? null : examList.get(0));
    }
}
