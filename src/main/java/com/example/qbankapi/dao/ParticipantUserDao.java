package com.example.qbankapi.dao;

import com.example.qbankapi.entity.ParticipantUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<ParticipantUser> findById(Long id) {
        List<ParticipantUser> participantUserList = entityManager.createQuery("SELECT u FROM ParticipantUser u WHERE u.id = :id", ParticipantUser.class).setParameter("id", id).getResultList();
        return participantUserList.isEmpty() ? Optional.empty() : Optional.of(participantUserList.getFirst());
    }

    public void update(ParticipantUser participantUser) {
        entityManager.merge(participantUser);
    }

//    public void save(User user) {
//        entityManager.persist(user);
//    }
//
//    public List<User> findAll() {
//        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
//    }

//    public Optional<User> findByUsername(String username) {
//        List<User> userList = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getResultList();
//        return Optional.ofNullable(userList.size() == 0 ? null : userList.get(0));
//    }

}
