package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamSubmission;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ParticipantUserExamSubmissionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ParticipantUserExamSubmission participantUserExamSubmission) {
        entityManager.persist(participantUserExamSubmission);
    }

}
