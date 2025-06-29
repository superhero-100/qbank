package com.example.qbankapi.dao;

import com.example.qbankapi.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

//    public void save(User user) {
//        entityManager.persist(user);
//    }
//
//    public List<User> findAll() {
//        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
//    }

    public Optional<User> findById(Long id) {
        List<User> userList = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getResultList();
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.getFirst());
    }

//    public Optional<User> findByUsername(String username) {
//        List<User> userList = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getResultList();
//        return Optional.ofNullable(userList.size() == 0 ? null : userList.get(0));
//    }

    public void update(User user) {
        entityManager.merge(user);
    }

}
