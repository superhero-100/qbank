package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminDao;
import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.TeacherDao;
import com.example.qbankapi.dao.UserDao;
import com.example.qbankapi.dto.request.LoginBaseUserRequestDto;
import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.Teacher;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.exception.AccountNotActiveException;
import com.example.qbankapi.exception.AdminNotFoundException;
import com.example.qbankapi.exception.TeacherNotFoundException;
import com.example.qbankapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BaseUserDao baseUserDao;
    private final AdminDao adminDao;
    private final TeacherDao teacherDao;
    private final UserDao userDao;

    @Transactional
    public Optional<BaseUser> authenticateBaseUser(LoginBaseUserRequestDto loginBaseUserRequest) {
        Optional<BaseUser> optionalUser;
        if (!loginBaseUserRequest.getBaseUserIdentifier().contains("@")) {
            log.info("Authenticate Attempt for BaseUser with username: {}", loginBaseUserRequest.getBaseUserIdentifier());
            optionalUser = baseUserDao.findByUsername(loginBaseUserRequest.getBaseUserIdentifier());
        } else if (loginBaseUserRequest.getBaseUserIdentifier().contains("@")) {
            log.info("Authenticate Attempt for BaseUser with email: {}", loginBaseUserRequest.getBaseUserIdentifier());
            optionalUser = baseUserDao.findByEmail(loginBaseUserRequest.getBaseUserIdentifier());
        } else {
            optionalUser = Optional.empty();
        }

        if (optionalUser.isPresent()) {
            BaseUser baseUser = optionalUser.get();
            if (!baseUser.getStatus().equals(BaseUser.Status.ACTIVE)) {
                log.warn("User account with identifier: {} is {}", loginBaseUserRequest.getBaseUserIdentifier(), baseUser.getStatus());
                throw new AccountNotActiveException("your account is locked or inactive");
            }
            log.info("BaseUser with username: {}, email: {} found", baseUser.getUsername(), baseUser.getEmail());
            if (baseUser.getPassword().equals(loginBaseUserRequest.getPassword())) {
                log.info("Authenticate attempt for BaseUser with identifier: {} successful", loginBaseUserRequest.getBaseUserIdentifier());
                if (loginBaseUserRequest.getZoneId() == null || !ZoneId.getAvailableZoneIds().contains(loginBaseUserRequest.getZoneId())) {
                    loginBaseUserRequest.setZoneId(ZoneId.systemDefault().getId());
                }
                switch (baseUser.getRole()) {
                    case ADMIN -> {
                        Admin admin = adminDao.findById(baseUser.getId())
                                .orElseThrow(() -> new AdminNotFoundException(
                                        String.format("Admin not found with id %d", baseUser.getId())
                                ));
                        admin.setZoneId(loginBaseUserRequest.getZoneId());
                        admin.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                        adminDao.update(admin);
                        log.info("Admin with id: {} timezone id: {} updated", admin.getId(), loginBaseUserRequest.getZoneId());
                    }
                    case TEACHER -> {
                        Teacher teacher = teacherDao.findById(baseUser.getId())
                                .orElseThrow(() -> new TeacherNotFoundException(
                                        String.format("Admin not found with id %d", baseUser.getId())
                                ));
                        teacher.setZoneId(loginBaseUserRequest.getZoneId());
                        teacher.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                        teacherDao.update(teacher);
                        log.info("Teacher with id: {} timezone id: {} updated", teacher.getId(), loginBaseUserRequest.getZoneId());
                    }
                    case USER -> {
                        User user = userDao.findById(baseUser.getId())
                                .orElseThrow(() -> new UserNotFoundException(
                                        String.format("User not found with id %d", baseUser.getId())
                                ));
                        user.setZoneId(loginBaseUserRequest.getZoneId());
                        user.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                        userDao.update(user);
                        log.info("User with id: {} timezone id: {} updated", user.getId(), loginBaseUserRequest.getZoneId());
                    }
                    default -> log.error("Unhandled role: {} for timezone checking ", baseUser.getRole());
                }
                return Optional.of(baseUser);
            } else {
                log.warn("Authenticate attempt for BaseUser with identifier: {} fail due to invalid password", loginBaseUserRequest.getBaseUserIdentifier());
            }
        } else {
            log.debug("BaseUser with identifier: {} not found", loginBaseUserRequest.getBaseUserIdentifier());
        }
        return Optional.empty();
    }

}
