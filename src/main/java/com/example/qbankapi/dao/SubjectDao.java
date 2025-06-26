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

//    public void update(Subject subject) {
//        entityManager.merge(subject);
//    }

    public List<Subject> findAll() {
        log.trace("Entering findAll()");
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
        log.trace("Exiting findAll()");
        return subjectList;
    }

    public Optional<Subject> findByName(String name) {
        log.trace("Entering findByName() with name: {}", name);
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s WHERE s.name = :name", Subject.class).setParameter("name", name).getResultList();
        Optional<Subject> optionalSubject = subjectList.isEmpty() ? Optional.empty() : Optional.of(subjectList.getFirst());
        log.trace("Exiting findAll()");
        return optionalSubject;
    }

    public void save(Subject subject) {
        log.trace("Entering save() with subject: {}", subject);
        entityManager.persist(subject);
        log.trace("Exiting save()");
    }

    public void update(Subject subject) {
        entityManager.merge(subject);
    }

    public Optional<Subject> findById(Long id) {
        List<Subject> subjectList = entityManager.createQuery("SELECT s FROM Subject s WHERE s.id = :id", Subject.class).setParameter("id", id).getResultList();
        return subjectList.isEmpty() ? Optional.empty() : Optional.of(subjectList.getFirst());
    }

}
