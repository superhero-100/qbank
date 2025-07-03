package com.example.qbankapi.dao;

import com.example.qbankapi.entity.InstructorUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class InstructorUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<InstructorUser> findById(Long id) {
        List<InstructorUser> instructorUserList = entityManager.createQuery("SELECT u FROM InstructorUser u WHERE u.id = :id", InstructorUser.class).setParameter("id", id).getResultList();
        return instructorUserList.isEmpty() ? Optional.empty() : Optional.of(instructorUserList.getFirst());
    }

    public void update(InstructorUser instructorUser) {
        entityManager.merge(instructorUser);
    }

}
