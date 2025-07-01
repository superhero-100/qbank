package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dto.model.UserFilterDto;
import com.example.qbankapi.dto.model.BaseUserViewPageDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseUserService {

    private final BaseUserDao baseUserDao;

    public BaseUserViewPageDto getFilteredUsers(UserFilterDto userFilterDto) {
        if (userFilterDto.getRole() == null || userFilterDto.getRole().isBlank())
            userFilterDto.setRole("ALL");
        if (userFilterDto.getStatus() == null || userFilterDto.getStatus().isBlank())
            userFilterDto.setStatus("ALL");
        if (userFilterDto.getUsernameRegxPattern() == null)
            userFilterDto.setUsernameRegxPattern("");
        if (userFilterDto.getSortBy() == null || userFilterDto.getSortBy().isBlank())
            userFilterDto.setSortBy("id");
        if (userFilterDto.getSortOrder() == null || userFilterDto.getSortOrder().isBlank())
            userFilterDto.setSortOrder("ASC");
        if (userFilterDto.getPageSize() == null || userFilterDto.getPageSize() <= 0) userFilterDto.setPageSize(10);
        if (userFilterDto.getPageSize() != 5 && userFilterDto.getPageSize() != 10 && userFilterDto.getPageSize() != 20)
            userFilterDto.setPageSize(10);
        if (userFilterDto.getPage() == null || userFilterDto.getPage() < 0) userFilterDto.setPage(0);

        System.out.println("role = " + Optional.ofNullable(userFilterDto.getRole()));
        System.out.println("status = " + Optional.ofNullable(userFilterDto.getStatus()));
        System.out.println("sortBy = " + Optional.ofNullable(userFilterDto.getSortBy()));
        System.out.println("sortOrder = " + Optional.ofNullable(userFilterDto.getSortOrder()));
        System.out.println("size = " + Optional.ofNullable(userFilterDto.getPageSize()));
        System.out.println("page = " + Optional.ofNullable(userFilterDto.getPage()));

        return baseUserDao.findFilteredUsers(userFilterDto);
    }

    @Transactional
    public void activateUser(Long id) {
        BaseUser baseUser = baseUserDao.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d")));
        baseUser.setStatus(BaseUser.Status.ACTIVE);
        baseUserDao.update(baseUser);
    }

    @Transactional
    public void inactivateUser(Long id) {
        BaseUser baseUser = baseUserDao.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d")));
        baseUser.setStatus(BaseUser.Status.INACTIVE);
        baseUserDao.update(baseUser);
    }

}
