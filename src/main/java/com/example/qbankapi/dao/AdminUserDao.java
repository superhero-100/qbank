package com.example.qbankapi.dao;

import com.example.qbankapi.entity.AdminUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<AdminUser> findById(Long id) {
        List<AdminUser> adminUserList = entityManager.createQuery("SELECT u FROM AdminUser u WHERE u.id = :id", AdminUser.class).setParameter("id", id).getResultList();
        return adminUserList.isEmpty() ? Optional.empty() : Optional.of(adminUserList.getFirst());
    }

    public void update(AdminUser adminUser) {
        entityManager.merge(adminUser);
    }

}
