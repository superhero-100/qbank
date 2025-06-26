package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dto.request.LoginUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BaseUserDao baseUserDao;

    @Transactional(readOnly = true)
    public Optional<BaseUser> authenticate(LoginUserRequestDto requestDto) {
        log.info("Authenticate Attempt for User with username: {}", requestDto.getUsername());
        Optional<BaseUser> optionalUser = baseUserDao.findByUsername(requestDto.getUsername());
        if (optionalUser.isPresent()) {
            BaseUser user = optionalUser.get();
            log.info("User with username: {} found", user.getUsername());
            if (user.getPassword().equals(requestDto.getPassword())) {
                log.info("Authenticate attempt for user with username: {} successful", user.getUsername());
                return Optional.of(user);
            } else {
                log.warn("Authenticate attempt for user with username: {} fail due to invalid password", user.getUsername());
            }
        } else {
            log.debug("User with username: {} not found", requestDto.getUsername());
        }
        return Optional.empty();
    }

}
