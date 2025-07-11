package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminUserDao;
import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dao.ParticipantUserDao;
import com.example.qbankapi.dto.request.LoginBaseUserRequestDto;
import com.example.qbankapi.entity.AdminUser;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.InstructorUser;
import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.exception.base.impl.BaseUserAccountNotActiveException;
import com.example.qbankapi.exception.base.impl.AdminUserNotFoundException;
import com.example.qbankapi.exception.base.impl.InstructorUserNotFoundException;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BaseUserDao baseUserDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminUserDao adminUserDao;
    private final InstructorUserDao instructorUserDao;
    private final ParticipantUserDao participantUserDao;

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
                log.warn("BaseUser account with identifier: {} is {}", loginBaseUserRequest.getBaseUserIdentifier(), baseUser.getStatus());
                throw new BaseUserAccountNotActiveException("your account is locked or inactive");
            }
            log.info("BaseUser with username: {}, email: {} found", baseUser.getUsername(), baseUser.getEmail());
            if (passwordEncoder.matches(loginBaseUserRequest.getPassword(), baseUser.getPassword())) {
                log.info("Authenticate attempt for BaseUser with identifier: {} successful", loginBaseUserRequest.getBaseUserIdentifier());
                if (loginBaseUserRequest.getZoneId() == null || !ZoneId.getAvailableZoneIds().contains(loginBaseUserRequest.getZoneId())) {
                    loginBaseUserRequest.setZoneId(ZoneId.systemDefault().getId());
                }
                switch (baseUser.getRole()) {
                    case ADMIN -> {
                        AdminUser adminUser = adminUserDao.findById(baseUser.getId())
                                .orElseThrow(() -> new AdminUserNotFoundException(
                                        String.format("Admin user not found with id %d", baseUser.getId())
                                ));
                        adminUser.setZoneId(loginBaseUserRequest.getZoneId());
                        adminUserDao.update(adminUser);
                        log.info("Admin with id: {} timezone id: {} updated", adminUser.getId(), loginBaseUserRequest.getZoneId());
                    }
                    case INSTRUCTOR -> {
                        InstructorUser instructorUser = instructorUserDao.findById(baseUser.getId())
                                .orElseThrow(() -> new InstructorUserNotFoundException(
                                        String.format("Instructor user not found with id %d", baseUser.getId())
                                ));
                        instructorUser.setZoneId(loginBaseUserRequest.getZoneId());
                        instructorUserDao.update(instructorUser);
                        log.info("Instructor with id: {} timezone id: {} updated", instructorUser.getId(), loginBaseUserRequest.getZoneId());
                    }
                    case PARTICIPANT -> {
                        ParticipantUser participantUser = participantUserDao.findById(baseUser.getId())
                                .orElseThrow(() -> new ParticipantUserNotFoundException(
                                        String.format("Participant not found with id %d", baseUser.getId())
                                ));
                        participantUser.setZoneId(loginBaseUserRequest.getZoneId());
                        participantUserDao.update(participantUser);
                        log.info("BaseUser with id: {} timezone id: {} updated", participantUser.getId(), loginBaseUserRequest.getZoneId());
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
