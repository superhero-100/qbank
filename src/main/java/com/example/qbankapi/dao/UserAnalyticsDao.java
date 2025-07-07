package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUserExamAnalytics;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserAnalyticsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ParticipantUserExamAnalytics analytics) {
        entityManager.persist(analytics);
    }

    public void update(ParticipantUserExamAnalytics analytics) {
        entityManager.merge(analytics);
    }

}