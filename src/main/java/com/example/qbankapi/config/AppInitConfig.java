package com.example.qbankapi.config;

import com.example.qbankapi.entity.Admin;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.service.AppInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
        ).forEach(user -> initService.createUserIfNotExists(user));

        log.info("Creating default subjects if not exists");
        List.of(
                createSubject("Data Structures", "Organizing and storing data for efficient access and modification."),
                createSubject("Algorithms", "Step-by-step procedures for solving computational problems."),
                createSubject("Database Management Systems", "Designing and managing structured data using relational models."),
                createSubject("Operating Systems", "Managing hardware and software resources in computing environments."),
                createSubject("Computer Networks", "Transmission of data and communication between connected systems."),
                createSubject("Web Development", "Building and maintaining websites and web applications.")
        ).forEach(subject -> initService.createSubjectIfNotExists(subject));

    }

    private Subject createSubject(String name, String description) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription(description);
        subject.setQuestions(List.of());
        subject.setExams(List.of());
        return subject;
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
