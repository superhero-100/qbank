package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.Subject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppInitService {

    private final BaseUserDao baseUserDao;
    private final SubjectDao subjectDao;

    @Transactional
    public void createUserIfNotExists(BaseUser baseUser) {
        baseUserDao.findByUsername(baseUser.getUsername()).ifPresentOrElse(user -> log.info("User with username: {} exists", user.getUsername()), () -> {
            baseUserDao.save(baseUser);
            log.info("User with username: {} created", baseUser.getUsername());
        });
    }

    @Transactional
    public void createSubjectIfNotExists(Subject subject) {
        subjectDao.findByName(subject.getName()).ifPresentOrElse(sub -> log.info("Subject with name: {} exists", sub.getName()), () -> {
            subjectDao.save(subject);
            log.info("Subject with name: {} created", subject.getName());
        });
    }

}
