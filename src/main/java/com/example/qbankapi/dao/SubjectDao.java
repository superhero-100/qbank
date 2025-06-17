package com.example.qbankapi.dao;

import com.example.qbankapi.dto.AddSubjectRequestDto;
import com.example.qbankapi.entity.Subject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SubjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAll(List<String> subjectList) {
        subjectList.stream()
                .forEach(subject -> entityManager.persist(
                        Subject.builder()
                                .name(subject)
                                .build()
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

}
