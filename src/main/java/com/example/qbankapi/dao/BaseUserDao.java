package com.example.qbankapi.dao;

import com.example.qbankapi.entity.BaseUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<BaseUser> findByUsername(String username) {
        List<BaseUser> userList = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", BaseUser.class).setParameter("username", username).getResultList();
        return Optional.ofNullable(userList.size() == 0 ? null : userList.get(0));
    }

}
