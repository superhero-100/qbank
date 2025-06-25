package com.example.qbankapi.dao;

import com.example.qbankapi.entity.UserExamResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
//        return Optional.ofNullable(userExamResultList.size() == 0 ? null : userExamResultList.get(0));
//    }

}
