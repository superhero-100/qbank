package com.example.qbankapi.service;

import com.example.qbankapi.dao.AdminUserDao;
import com.example.qbankapi.dao.InstructorUserDao;
import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.model.SubjectDto;
import com.example.qbankapi.dto.request.AddSubjectRequestDto;
import com.example.qbankapi.dto.request.UpdateSubjectRequestDto;
import com.example.qbankapi.dto.view.SubjectAssignedInstructorsViewDto;
import com.example.qbankapi.dto.view.SubjectViewDto;
import com.example.qbankapi.entity.AdminUser;
import com.example.qbankapi.entity.InstructorUser;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.base.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectDao subjectDao;
    private final AdminUserDao adminUserDao;
    private final InstructorUserDao instructorUserDao;

    @Transactional(readOnly = true)
    public List<SubjectViewDto> getSubjectViewDtoList() {
        List<Subject> subjectList = subjectDao.findAll();
        log.debug("Fetched [{}] subjects from DB", subjectList.size());
        return subjectList.stream()
                .map(subject -> SubjectViewDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectAssignedInstructorsViewDto getSubjectAssignedInstructorsDtoById(Long subjectId, String currentUserZoneId) {
        Subject subject = subjectDao.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", subjectId)));
        log.debug("Fetched subject with id [{}] from DB", subjectId);
        return SubjectAssignedInstructorsViewDto.builder()
                .subject(SubjectViewDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .build())
                .instructorUsers(subject.getAssignedInstructors()
                        .stream()
                        .map(instructorUser -> SubjectAssignedInstructorsViewDto.InstructorUserViewDto.builder()
                                .id(instructorUser.getId())
                                .username(instructorUser.getUsername())
                                .email(instructorUser.getEmail())
                                .zoneId(instructorUser.getZoneId())
                                .registeredAt(instructorUser.getCreatedAt().withZoneSameInstant(ZoneId.of(currentUserZoneId)))
//                                ----
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void adminAddSubject(AddSubjectRequestDto addSubjectRequest, Long adminUserId) {
        AdminUser adminUser = adminUserDao.findById(adminUserId)
                .orElseThrow(() -> new AdminUserNotFoundException(String.format("Admin user not found with id [%d]", adminUserId)));

        if (subjectDao.findByName(addSubjectRequest.getName()).isPresent()) {
            log.warn("Subject already exists with name [{}]", addSubjectRequest.getName());
            throw new SubjectAlreadyExistsException(String.format("Subject already exists with name [%s]", addSubjectRequest.getName()));
        }

        Subject subject = new Subject();

        subject.setName(addSubjectRequest.getName());
        subject.setDescription(addSubjectRequest.getDescription());
        subject.setQuestions(List.of());
        subject.setExams(List.of());
        subject.setAssignedInstructors(List.of());
        subject.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        subject.setCreationZone(adminUser.getZoneId());

        subjectDao.save(subject);
        log.debug("Subject created with name [{}]", addSubjectRequest.getName());
    }

    @Transactional(readOnly = true)
    public SubjectDto getSubjectDtoById(Long subjectId) {
        Subject subject = subjectDao.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", subjectId)));
        log.debug("Subject found with id [{}]", subjectId);

        return SubjectDto.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .build();
    }

    @Transactional
    public void adminUpdateSubject(UpdateSubjectRequestDto updateSubjectRequest) {
        Subject subject = subjectDao.findById(updateSubjectRequest.getId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id [%d]", updateSubjectRequest.getId())));

        subject.setName(updateSubjectRequest.getName());
        subject.setDescription(updateSubjectRequest.getDescription());

        subjectDao.update(subject);
        log.debug("Subject updated with name [{}]", updateSubjectRequest.getName());
    }

    @Transactional(readOnly = true)
    public List<SubjectViewDto> getAvailableSubjectViewDtoList(Set<Long> assignedSubjectIds) {
        return subjectDao.findAll()
                .stream()
                .filter(subject -> !assignedSubjectIds.contains(subject.getId()))
                .map(subject -> SubjectViewDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    //
    @Transactional(readOnly = true)
    public List<SubjectViewDto> getAssignedSubjectViewDtoListForInstructor(Long instructorUserId) {
        InstructorUser instructorUser = instructorUserDao.findById(instructorUserId).orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id: %d", instructorUserId)));
        return instructorUser.getAssignedSubjects()
                .stream()
                .map(subject -> SubjectViewDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public void isSubjectAssigned(Long instructorId, Long subjectId) {
//        InstructorUser instructorUser = instructorUserDao.findById(instructorId).orElseThrow(() -> new InstructorUserNotFoundException(String.format("Instructor user not found with id: %d", instructorId)));
//
//        subjectDao.findById(subjectId).orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id: %d", subjectId)));
//
//        boolean isAssigned = instructorUser.getAssignedSubjects().stream()
//                .anyMatch(subject -> subject.getId().equals(subjectId));
//
//        if (!isAssigned) {
//            throw new AccessDeniedException(String.format("Instructor %d is not assigned to subject %d", instructorId, subjectId));
//        }
//    }


//    ---

//    @Transactional(readOnly = true)
//    public SubjectDto getInDtoById(Long id) {
//        Subject subject = subjectDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
//        return SubjectDto.builder().id(subject.getId()).name(subject.getName()).totalAvailableExams(subject.getExams().size()).totalAvailableQuestions(subject.getQuestions().size()).build();
//    }
//
//    @Transactional(readOnly = true)
//    public List<SubjectDto> getAllInDto() {
//        return subjectDao.findAll().stream().map(subject -> SubjectDto.builder().id(subject.getId()).name(subject.getName()).totalAvailableExams(subject.getExams().size()).totalAvailableQuestions(subject.getQuestions().size()).build()).collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void addAll(List<String> subjectList) {
//        subjectList.stream().forEach(subject -> subjectDao.save(
//                Subject.builder()
//                        .name(subject)
//                        .build()
//                )
//        );
//    }

}
