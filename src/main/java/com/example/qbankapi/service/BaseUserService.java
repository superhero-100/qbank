package com.example.qbankapi.service;

import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dto.request.RegisterBaseUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.InstructorUser;
import com.example.qbankapi.entity.ParticipantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseUserService {

    private final BaseUserDao baseUserDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean validateUsernameIfExists(String username) {
        log.debug("Validating existence of username: {}", username);
        boolean exists = baseUserDao.findByUsername(username).isPresent();
        log.info("Username '{}' exists: {}", username, exists);
        return exists;
    }

    @Transactional(readOnly = true)
    public boolean validateEmailIfExists(String email) {
        log.debug("Validating existence of email: {}", email);
        boolean exists = baseUserDao.findByEmail(email).isPresent();
        log.info("Email '{}' exists: {}", email, exists);
        return exists;
    }

    @Transactional
    public Optional<BaseUser> registerBaseUser(RegisterBaseUserRequestDto registerBaseUserRequest) {
        log.info("Initiating user registration for username: {}, role: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getRole());

        if (registerBaseUserRequest.getZoneId() == null || !ZoneId.getAvailableZoneIds().contains(registerBaseUserRequest.getZoneId())) {
            log.warn("Invalid or null Zone ID '{}'. Using system default.", registerBaseUserRequest.getZoneId());
            registerBaseUserRequest.setZoneId(ZoneId.systemDefault().getId());
        }

        switch (registerBaseUserRequest.getRole()) {
            case INSTRUCTOR -> {
                InstructorUser instructorUser = new InstructorUser();
                instructorUser.setUsername(registerBaseUserRequest.getUsername());
                instructorUser.setEmail(registerBaseUserRequest.getEmail());
                instructorUser.setPassword(passwordEncoder.encode(registerBaseUserRequest.getPassword()));
                instructorUser.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
                instructorUser.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                instructorUser.setStatus(BaseUser.Status.ACTIVE);
                instructorUser.setZoneId(registerBaseUserRequest.getZoneId());

                baseUserDao.save(instructorUser);
                log.info("Instructor user '{}' registered successfully.", instructorUser.getUsername());
                return Optional.of(instructorUser);
            }
            case PARTICIPANT -> {
                ParticipantUser participantUser = new ParticipantUser();
                participantUser.setUsername(registerBaseUserRequest.getUsername());
                participantUser.setEmail(registerBaseUserRequest.getEmail());
                participantUser.setPassword(passwordEncoder.encode(registerBaseUserRequest.getPassword()));
                participantUser.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
                participantUser.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
                participantUser.setEnrolledExams(List.of());
                participantUser.setCompletedExams(List.of());
                participantUser.setUserExamResults(List.of());
                participantUser.setStatus(BaseUser.Status.ACTIVE);
                participantUser.setZoneId(registerBaseUserRequest.getZoneId());

                baseUserDao.save(participantUser);
                log.info("Participant user '{}' registered successfully.", participantUser.getUsername());
                return Optional.of(participantUser);
            }
            default -> {
                log.error("User registration failed: unsupported role '{}'", registerBaseUserRequest.getRole());
                return Optional.empty();
            }
        }
    }

//    private final BaseUserDao baseUserDao;

//    public BaseUserViewPageDto getFilteredUsers(UserFilterDto userFilterDto) {
//        if (userFilterDto.getRole() == null || userFilterDto.getRole().isBlank())
//            userFilterDto.setRole("ALL");
//        if (userFilterDto.getStatus() == null || userFilterDto.getStatus().isBlank())
//            userFilterDto.setStatus("ALL");
//        if (userFilterDto.getUsernameRegxPattern() == null)
//            userFilterDto.setUsernameRegxPattern("");
//        if (userFilterDto.getSortBy() == null || userFilterDto.getSortBy().isBlank())
//            userFilterDto.setSortBy("id");
//        if (userFilterDto.getSortOrder() == null || userFilterDto.getSortOrder().isBlank())
//            userFilterDto.setSortOrder("ASC");
//        if (userFilterDto.getPageSize() == null || userFilterDto.getPageSize() <= 0) userFilterDto.setPageSize(10);
//        if (userFilterDto.getPageSize() != 5 && userFilterDto.getPageSize() != 10 && userFilterDto.getPageSize() != 20)
//            userFilterDto.setPageSize(10);
//        if (userFilterDto.getPage() == null || userFilterDto.getPage() < 0) userFilterDto.setPage(0);
//
//        System.out.println("role = " + Optional.ofNullable(userFilterDto.getRole()));
//        System.out.println("status = " + Optional.ofNullable(userFilterDto.getStatus()));
//        System.out.println("sortBy = " + Optional.ofNullable(userFilterDto.getSortBy()));
//        System.out.println("sortOrder = " + Optional.ofNullable(userFilterDto.getSortOrder()));
//        System.out.println("size = " + Optional.ofNullable(userFilterDto.getPageSize()));
//        System.out.println("page = " + Optional.ofNullable(userFilterDto.getPage()));
//
//        return baseUserDao.findFilteredUsers(userFilterDto);
//    }
//
//    @Transactional
//    public void activateUser(Long id) {
//        BaseUser baseUser = baseUserDao.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d")));
//        baseUser.setStatus(BaseUser.Status.ACTIVE);
//        baseUserDao.update(baseUser);
//    }
//
//    @Transactional
//    public void inactivateUser(Long id) {
//        BaseUser baseUser = baseUserDao.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d")));
//        baseUser.setStatus(BaseUser.Status.INACTIVE);
//        baseUserDao.update(baseUser);
//    }

}
