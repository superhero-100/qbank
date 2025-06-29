package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ExamAnalytics;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ExamAnalyticsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ExamAnalytics analytics) {
        entityManager.persist(analytics);
    }

    public void update(ExamAnalytics analytics) {
        entityManager.merge(analytics);
    }

}
