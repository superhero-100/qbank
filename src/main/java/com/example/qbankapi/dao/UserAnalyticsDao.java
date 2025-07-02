package com.example.qbankapi.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserAnalyticsDao {

    @PersistenceContext
    private EntityManager entityManager;

//    public void save(UserAnalytics analytics) {
//        entityManager.persist(analytics);
//    }
//
//    public void update(UserAnalytics analytics) {
//        entityManager.merge(analytics);
//    }

}