package com.example.qbankapi.config;

import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.service.AppInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInitConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final AppInitService initService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Creating default users if not exists");
        List.of(
                createAdmin("admin", "admin@123"),
                createUser("sahil", "sahil@123"),
                createUser("jetha", "jetha@123"),
                createUser("daya", "daya@123"),
                createUser("champak", "champak@123"),
                createUser("tapu", "tapu@123")
        ).forEach(user -> {
            initService.createUserIfNotExists(user);
        });
    }

    private Admin createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setModifiedAt(LocalDateTime.now());
        return admin;
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setEnrolledExams(List.of());
        user.setUserExamResults(List.of());
        return user;
    }

}
