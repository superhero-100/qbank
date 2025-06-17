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

    public void addAll(List<AddUserRequestDto> userRequestDtoList) {
        userDao.saveAll(userRequestDtoList);
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    public Optional<User> getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException());
    }

}
