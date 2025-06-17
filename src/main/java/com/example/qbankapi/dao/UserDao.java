package com.example.qbankapi.dao;

import com.example.qbankapi.dto.AddUserRequestDto;
import com.example.qbankapi.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAll(List<AddUserRequestDto> userRequestDtoList) {
        userRequestDtoList.stream().forEach(
                user -> entityManager.persist(
                        User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .build()
                )
        );
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getResultList().get(0));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getResultList().get(0));
    }

}
