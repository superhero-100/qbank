package com.example.qbankapi.service;

import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.ExamDetailsDto;
import com.example.qbankapi.dto.SubjectDto;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectDao subjectDao;

    @Transactional(readOnly = true)
    public List<Subject> getAll() {
        return subjectDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<ExamDetailsDto> getSubjectExamsInDtoById(Long id) {
        Subject subject = subjectDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
        return subject.getExams()
                .stream()
                .map(exam -> ExamDetailsDto.builder()
                        .id(exam.getId())
                        .description(exam.getDescription())
                        .totalMarks(exam.getTotalMarks())
                        .subjectName(exam.getSubject().getName())
                        .totalQuestions(exam.getQuestions().size())
                        .totalEnrolledUsers(exam.getEnrolledUsers().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectDto getInDtoById(Long id) {
        Subject subject = subjectDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
        return SubjectDto.builder().id(subject.getId()).name(subject.getName()).totalAvailableExams(subject.getExams().size()).totalAvailableQuestions(subject.getQuestions().size()).build();
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> getAllInDto() {
        return subjectDao.findAll().stream().map(subject -> SubjectDto.builder().id(subject.getId()).name(subject.getName()).totalAvailableExams(subject.getExams().size()).totalAvailableQuestions(subject.getQuestions().size()).build()).collect(Collectors.toList());
    }

    @Transactional
    public void addAll(List<String> subjectList) {
        subjectList.stream().forEach(subject -> subjectDao.save(
                Subject.builder()
                        .name(subject)
                        .build()
                )
        );
    }
}
