package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.entity.BaseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppInitService {

    private final BaseUserDao baseUserDao;

    @Transactional
    public void createUserIfNotExists(BaseUser baseUser) {
        baseUserDao.findByUsername(baseUser.getUsername()).ifPresentOrElse(user -> log.info("User with username: {} exists", user.getUsername()), () -> {
            baseUserDao.save(baseUser);
            log.info("User with username: {} created", baseUser.getUsername());
        });
    }

}
