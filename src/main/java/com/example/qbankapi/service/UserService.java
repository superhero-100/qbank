package com.example.qbankapi.service;

import com.example.qbankapi.dao.UserDao;
import com.example.qbankapi.dto.AddUserRequestDto;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    @Transactional
    public void addAll(List<AddUserRequestDto> userRequestDtoList) {
        userRequestDtoList.stream().forEach(
                user -> userDao.save(
                        User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .build()
                )
        );
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException());
    }

}
