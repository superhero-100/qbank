package com.example.qbankapi.dao;

import com.example.qbankapi.entity.UserAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(UserAnswer userAnswer) {
        entityManager.persist(userAnswer);
    }

}
