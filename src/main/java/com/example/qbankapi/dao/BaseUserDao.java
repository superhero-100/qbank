package com.example.qbankapi.dao;

import com.example.qbankapi.entity.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class BaseUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<BaseUser> findByUsername(String username) {
        log.trace("Entering findByUsername() with username: {}", username);
        List<BaseUser> userList = entityManager.createQuery("SELECT u FROM BaseUser u WHERE u.username = :username", BaseUser.class).setParameter("username", username).getResultList();
        Optional<BaseUser> optionalBaseUser = userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
        log.trace("Exiting findByUsername()");
        return optionalBaseUser;
    }

    public void save(BaseUser baseUser) {
        log.trace("Entering save() with username: {}", baseUser.getUsername());
        entityManager.persist(baseUser);
        log.trace("Exiting save()");
    }

}
