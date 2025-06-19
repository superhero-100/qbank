package com.example.qbankapi.dao;

import com.example.qbankapi.entity.UserAnalytics;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserAnalyticsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(UserAnalytics analytics) {
        entityManager.persist(analytics);
    }

}