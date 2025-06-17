package com.example.qbankapi.service;

import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dto.AddQuestionRequestDto;
import com.example.qbankapi.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;

    public List<Question> getAll() {
        return questionDao.findAll();
    }

    public void addAll(List<AddQuestionRequestDto> addQuestionRequestDtoList) {
        questionDao.saveAll(addQuestionRequestDtoList);
    }
}
