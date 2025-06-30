package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminDao;
import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.UserDao;
import com.example.qbankapi.dto.request.LoginUserRequestDto;
import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.exception.AdminNotFoundException;
import com.example.qbankapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BaseUserDao baseUserDao;
    private final UserDao userDao;
    private final AdminDao adminDao;

    @Transactional
    public Optional<BaseUser> authenticate(LoginUserRequestDto requestDto) {
        log.info("Authenticate Attempt for User with username: {}", requestDto.getUsername());
        Optional<BaseUser> optionalUser = baseUserDao.findByUsername(requestDto.getUsername());
        if (optionalUser.isPresent()) {
            BaseUser baseUser = optionalUser.get();
            log.info("User with username: {} found", baseUser.getUsername());
            if (baseUser.getPassword().equals(requestDto.getPassword())) {
                log.info("Authenticate attempt for user with username: {} successful", baseUser.getUsername());
                if (requestDto.getZoneId() == null || !ZoneId.getAvailableZoneIds().contains(requestDto.getZoneId())) {
                    requestDto.setZoneId("UTC");
                }
                switch (baseUser.getRole()) {
                    case USER -> {
                        User user = userDao.findById(baseUser.getId())
                                .orElseThrow(() -> new UserNotFoundException(
                                        String.format("User not found with id %d", baseUser.getId())
                                ));
                        user.setZoneId(requestDto.getZoneId());
                        user.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                        userDao.update(user);
                        log.info("User with id: {} timezone id: {} updated", user.getId(), requestDto.getZoneId());
                    }
                    case ADMIN -> {
                        Admin admin = adminDao.findById(baseUser.getId())
                                .orElseThrow(() -> new AdminNotFoundException(
                                        String.format("Admin not found with id %d", baseUser.getId())
                                ));
                        admin.setZoneId(requestDto.getZoneId());
                        admin.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                        adminDao.update(admin);
                        log.info("Admin with id: {} timezone id: {} updated", admin.getId(), requestDto.getZoneId());
                    }
                    default -> log.error("Unhandled role: {} for timezone checking ", baseUser.getRole());
                }
                return Optional.of(baseUser);
            } else {
                log.warn("Authenticate attempt for user with username: {} fail due to invalid password", baseUser.getUsername());
            }
        } else {
            log.debug("User with username: {} not found", requestDto.getUsername());
        }
        return Optional.empty();
    }

}
