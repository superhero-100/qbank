package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminUserDao;
import com.example.qbankapi.dao.BaseUserDao;
import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dao.ParticipantUserDao;
import com.example.qbankapi.dto.model.BaseUserFilterDto;
import com.example.qbankapi.dto.request.RegisterBaseUserRequestDto;
import com.example.qbankapi.dto.view.BaseUserPageViewDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.InstructorUser;
import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.exception.base.BaseUserNotFoundException;
import com.example.qbankapi.exception.base.impl.*;
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
    private final AdminUserDao adminUserDao;
    private final InstructorUserDao instructorUserDao;
    private final ParticipantUserDao participantUserDao;

    @Transactional
    public Optional<BaseUser> registerBaseUser(RegisterBaseUserRequestDto registerBaseUserRequest) {
        log.info("Initiating user registration for username [{}], role [{}]", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getRole());

        if (baseUserDao.findByUsername(registerBaseUserRequest.getUsername()).isPresent()) {
            log.warn("Registration failed: username [{}] already exists", registerBaseUserRequest.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        if (baseUserDao.findByEmail(registerBaseUserRequest.getEmail()).isPresent()) {
            log.warn("Registration failed: email [{}] already exists", registerBaseUserRequest.getEmail());
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (!registerBaseUserRequest.getPassword().equals(registerBaseUserRequest.getConfirmPassword())) {
            log.warn("Registration failed: password and confirm password do not match for username [{}]", registerBaseUserRequest.getUsername());
            throw new PasswordMismatchException("Password and confirm password do not match.");
        }

        if (registerBaseUserRequest.getZoneId() == null || !ZoneId.getAvailableZoneIds().contains(registerBaseUserRequest.getZoneId())) {
            log.warn("Invalid or null Zone ID [{}]. Using system default.", registerBaseUserRequest.getZoneId());
            registerBaseUserRequest.setZoneId(ZoneId.systemDefault().getId());
        }

        switch (registerBaseUserRequest.getRole()) {
            case INSTRUCTOR -> {
                InstructorUser instructorUser = new InstructorUser();
                instructorUser.setUsername(registerBaseUserRequest.getUsername());
                instructorUser.setEmail(registerBaseUserRequest.getEmail());
                instructorUser.setPassword(passwordEncoder.encode(registerBaseUserRequest.getPassword()));
                instructorUser.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
                instructorUser.setCreationZone(registerBaseUserRequest.getZoneId());
                instructorUser.setStatus(BaseUser.Status.ACTIVE);
                instructorUser.setZoneId(registerBaseUserRequest.getZoneId());
                instructorUser.setAssignedSubjects(List.of());
                instructorUser.setCreatedQuestions(List.of());
                instructorUser.setCreatedExams(List.of());

                instructorUser.setRoleValue(BaseUser.Role.INSTRUCTOR.name());

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
                participantUser.setCreationZone(registerBaseUserRequest.getZoneId());
                participantUser.setParticipantUserExamResults(List.of());
                participantUser.setStatus(BaseUser.Status.ACTIVE);
                participantUser.setZoneId(registerBaseUserRequest.getZoneId());
                participantUser.setExamEnrollments(List.of());
                participantUser.setParticipantUserExamSubmissions(List.of());
                participantUser.setParticipantUserExamResults(List.of());

                participantUser.setRoleValue(BaseUser.Role.PARTICIPANT.name());

                baseUserDao.save(participantUser);
                log.info("Participant user [{}] registered successfully.", participantUser.getUsername());

                return Optional.of(participantUser);
            }
            default -> {
                log.error("User registration failed: unsupported role [{}]", registerBaseUserRequest.getRole());
                return Optional.empty();
            }
        }
    }

    @Transactional(readOnly = true)
    public BaseUserPageViewDto getFilteredUsersForAdmin(BaseUserFilterDto userFilterDto) {
        log.info("Invoked getFilteredUsers with initial filter [{}]", userFilterDto);

        normalizeFilterDto(userFilterDto);

        BaseUserPageViewDto userPage = baseUserDao.findFilteredUsers(userFilterDto);
        log.info("Retrieved [{}] users with applied filters", userPage.getBaseUsers().size());

        return userPage;
    }

    @Transactional
    public void activateUser(Long baseUserId) {
        BaseUser baseUser = baseUserDao.findById(baseUserId)
                .orElseThrow(() -> new BaseUserNotFoundException(String.format("Base user not found with id [%d]", baseUserId)));

        baseUser.setStatus(BaseUser.Status.ACTIVE);

        baseUserDao.update(baseUser);
        log.info("Base user successfully activated with id [{}]", baseUserId);
    }

    @Transactional
    public void inactivateUser(Long baseUserId) {
        BaseUser baseUser = baseUserDao.findById(baseUserId)
                .orElseThrow(() -> new BaseUserNotFoundException(String.format("Base user not found with id [%d]", baseUserId)));

        baseUser.setStatus(BaseUser.Status.INACTIVE);

        baseUserDao.update(baseUser);
        log.info("Base user successfully inactivated with id [{}]", baseUserId);
    }

    private void normalizeFilterDto(BaseUserFilterDto userFilterDto) {
        if (userFilterDto.getRole() == null || userFilterDto.getRole().isBlank()) {
            log.debug("Missing role. Defaulting to 'ALL'");
            userFilterDto.setRole("ALL");
        }

        if (userFilterDto.getStatus() == null || userFilterDto.getStatus().isBlank()) {
            log.debug("Missing status. Defaulting to 'ALL'");
            userFilterDto.setStatus("ALL");
        }

        if (userFilterDto.getUsernameRegxPattern() == null) {
            log.debug("Missing username pattern. Defaulting to empty string");
            userFilterDto.setUsernameRegxPattern("");
        }

        if (userFilterDto.getStatusFilter() == null || userFilterDto.getStatusFilter().isBlank()) {
            log.debug("Missing statusFilter. Defaulting to 'ALL'");
            userFilterDto.setStatusFilter("ALL");
        }

        if (userFilterDto.getSortBy() == null || userFilterDto.getSortBy().isBlank()) {
            log.debug("Missing sortBy. Defaulting to 'id'");
            userFilterDto.setSortBy("id");
        }

        if (userFilterDto.getSortOrder() == null || userFilterDto.getSortOrder().isBlank()) {
            log.debug("Missing sortOrder. Defaulting to 'ASC'");
            userFilterDto.setSortOrder("ASC");
        }

        if (userFilterDto.getPageSize() == null || userFilterDto.getPageSize() <= 0) {
            log.debug("Missing or invalid pageSize. Defaulting to 10");
            userFilterDto.setPageSize(10);
        }

        if (userFilterDto.getPageSize() != 5 && userFilterDto.getPageSize() != 10 && userFilterDto.getPageSize() != 20) {
            log.warn("Unsupported pageSize: {}. Resetting to default (10)", userFilterDto.getPageSize());
            userFilterDto.setPageSize(10);
        }

        if (userFilterDto.getPage() == null || userFilterDto.getPage() < 0) {
            log.debug("Missing or invalid page number. Defaulting to 0");
            userFilterDto.setPage(0);
        }

        log.info("Final applied filters - role: {}, status: {}, usernamePattern: '{}', statusFilter: {}, sortBy: {}, sortOrder: {}, pageSize: {}, page: {}",
                userFilterDto.getRole(),
                userFilterDto.getStatus(),
                userFilterDto.getUsernameRegxPattern(),
                userFilterDto.getStatusFilter(),
                userFilterDto.getSortBy(),
                userFilterDto.getSortOrder(),
                userFilterDto.getPageSize(),
                userFilterDto.getPage());
    }

    @Transactional(readOnly = true)
    public String findByIdAndGetZoneId(Long baseUserId) {
        BaseUser baseUser = baseUserDao.findById(baseUserId).orElseThrow(() -> new BaseUserNotFoundException(String.format("Base user not found with id [%d]", baseUserId)));
        return switch (baseUser.getRole()) {
            case ADMIN -> adminUserDao.findById(baseUserId).orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin user not found with id [%d]", baseUserId))).getZoneId();
            case INSTRUCTOR -> instructorUserDao.findById(baseUserId).orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id [%d]", baseUserId))).getZoneId();
            case PARTICIPANT -> participantUserDao.findById(baseUserId).orElseThrow(() -> new ParticipantUserNotFoundException(String.format("Participant user not found with id [%d]", baseUserId))).getZoneId();
            default -> throw new InvalidProfileTypeException(String.format("Invalid id base user id [%d]", baseUserId));
        };
    }

}
