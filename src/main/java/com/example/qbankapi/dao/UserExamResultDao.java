package com.example.qbankapi.dao;

import com.example.qbankapi.entity.UserExamResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserExamResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(UserExamResult result) {
        entityManager.persist(result);
    }

}
