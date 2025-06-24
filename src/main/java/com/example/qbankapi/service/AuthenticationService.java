package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.UserDao;
import com.example.qbankapi.dto.LoginUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BaseUserDao baseUserDao;

    @Transactional(readOnly = true)
    public Optional<BaseUser> authenticate(LoginUserRequestDto requestDto) {
        Optional<BaseUser> optionalUser = baseUserDao.findByUsername(requestDto.getUsername());
        if (optionalUser.isPresent()) {
            BaseUser user = optionalUser.get();
            if (user.getPassword().equals(requestDto.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
