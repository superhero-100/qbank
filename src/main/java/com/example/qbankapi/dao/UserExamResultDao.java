package com.example.qbankapi.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserExamResultDao {

    @PersistenceContext
    private EntityManager entityManager;

//    public void save(UserExamResult result) {
//        entityManager.persist(result);
//    }
//
//    public Optional<UserExamResult> findById(Long id) {
//        List<UserExamResult> userExamResultList = entityManager.createQuery("SELECT uer FROM UserExamResult uer WHERE uer.id = :id", UserExamResult.class).setParameter("id", id).getResultList();
//        return userExamResultList.isEmpty() ? Optional.empty() : Optional.of(userExamResultList.getFirst());
//    }

}
