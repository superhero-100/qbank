package com.example.qbankapi.dao;

import com.example.qbankapi.entity.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class SubjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Subject> findByName(String name) {
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s WHERE s.name = :name", Subject.class).setParameter("name", name).getResultList();
        return subjectList.isEmpty() ? Optional.empty() : Optional.of(subjectList.getFirst());
    }

    public void save(Subject subject) {
        entityManager.persist(subject);
    }

    public List<Subject> findAll() {
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

    public Optional<Subject> findById(Long id) {
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s WHERE s.id = :id", Subject.class).setParameter("id", id).getResultList();
        return subjectList.isEmpty() ? Optional.empty() : Optional.of(subjectList.getFirst());
    }

    public void update(Subject subject) {
        entityManager.merge(subject);
    }

}
