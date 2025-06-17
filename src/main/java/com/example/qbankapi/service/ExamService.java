package com.example.qbankapi.service;

import com.example.qbankapi.dao.ExamDao;
import com.example.qbankapi.dto.CreateExamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamDao examDao;

    public void createExam(CreateExamRequestDto examDTO) {
        examDao.save(examDTO);
    }
}
