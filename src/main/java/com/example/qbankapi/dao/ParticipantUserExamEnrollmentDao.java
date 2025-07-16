package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamEnrollment;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.crypto.spec.OAEPParameterSpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantUserExamEnrollmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ParticipantUserExamEnrollment participantUserExamEnrollment) {
        entityManager.persist(participantUserExamEnrollment);
    }

    public Optional<ParticipantUserExamEnrollment> findById(Long examEnrollmentId) {
        List<ParticipantUserExamEnrollment> resultList = entityManager.createQuery("SELECT puee FROM ParticipantUserExamEnrollment puee WHERE puee.id = :examEnrollmentId", ParticipantUserExamEnrollment.class).setParameter("examEnrollmentId", examEnrollmentId).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.getFirst());
    }

}
