package com.example.qbankapi.dao;

import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Admin> findById(Long id) {
        List<Admin> adminList = entityManager.createQuery("SELECT a FROM Admin a WHERE a.id = :id", Admin.class).setParameter("id", id).getResultList();
        return adminList.isEmpty() ? Optional.empty() : Optional.of(adminList.getFirst());
    }

    public void update(Admin admin) {
        entityManager.merge(admin);
    }

}
