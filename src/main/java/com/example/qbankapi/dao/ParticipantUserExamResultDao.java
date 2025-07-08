package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantUserExamResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ParticipantUserExamResult result) {
        entityManager.persist(result);
    }

    public Optional<ParticipantUserExamResult> findById(Long id) {
        List<ParticipantUserExamResult> participantUserExamResultList = entityManager.createQuery("SELECT uer FROM ParticipantUserExamResult uer WHERE uer.id = :id", ParticipantUserExamResult.class).setParameter("id", id).getResultList();
        return participantUserExamResultList.isEmpty() ? Optional.empty() : Optional.of(participantUserExamResultList.getFirst());
    }

}
