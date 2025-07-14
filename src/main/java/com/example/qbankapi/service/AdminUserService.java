package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminUserDao;
import com.example.qbankapi.exception.base.impl.AdminUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

//    private final AdminUserDao adminUserDao;

//    public String getAdminZoneId(Long userId) {
//        return adminUserDao.findById(userId).orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin not found with id: %d", userId))).getZoneId();
//    }

}
