package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamQuestionAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ParticipantUserExamQuestionAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ParticipantUserExamQuestionAnswer participantUserExamQuestionAnswer) {
        entityManager.persist(participantUserExamQuestionAnswer);
    }

}
