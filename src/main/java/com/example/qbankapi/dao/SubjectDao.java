package com.example.qbankapi.dao;

import com.example.qbankapi.entity.Subject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class SubjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Subject subject) {
        entityManager.persist(subject);
    }

    public void update(Subject subject) {
        entityManager.merge(subject);
    }

    public List<Subject> findAll() {
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

    public Optional<Subject> findById(Long id) {
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s WHERE s.id = :id", Subject.class).setParameter("id", id).getResultList();
        return Optional.ofNullable(subjectList.size() == 0 ? null : subjectList.get(0));
    }

}
