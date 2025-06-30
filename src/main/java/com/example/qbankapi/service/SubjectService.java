package com.example.qbankapi.service;

import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.model.ExamDetailsDto;
import com.example.qbankapi.dto.model.SubjectDto;
import com.example.qbankapi.dto.request.AddSubjectRequestDto;
import com.example.qbankapi.dto.request.UpdateSubjectRequestDto;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.SubjectAlreadyExistsException;
import com.example.qbankapi.exception.SubjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectDao subjectDao;

    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectDtoList() {
        List<Subject> subjectList = subjectDao.findAll();
        log.debug("Fetched {} subjects from database", subjectList.size());
        return subjectList.stream()
                .map(subject -> SubjectDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createSubject(AddSubjectRequestDto addSubjectRequest) {
        if (subjectDao.findByName(addSubjectRequest.getName()).isPresent()) {
            throw new SubjectAlreadyExistsException(String.format("Subject already exists with name: %s", addSubjectRequest.getName()));
        }

        Subject subject = new Subject();
        subject.setName(addSubjectRequest.getName());
        subject.setDescription(addSubjectRequest.getDescription());
        subject.setQuestions(List.of());
        subject.setExams(List.of());
        subjectDao.save(subject);
    }

    @Transactional
    public void updateSubject(UpdateSubjectRequestDto updateSubjectRequest) {
        Subject subject = subjectDao.findById(updateSubjectRequest.getId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id: %d", updateSubjectRequest.getId())));
        subject.setName(updateSubjectRequest.getName());
        subject.setDescription(updateSubjectRequest.getDescription());
        subjectDao.update(subject);
    }

    @Transactional(readOnly = true)
    public SubjectDto getSubjectDtoById(Long id) {
        Subject subject = subjectDao.findById(id).orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id: %d",id)));
        return SubjectDto.builder().id(subject.getId()).name(subject.getName()).description(subject.getDescription()).build();
    }

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
