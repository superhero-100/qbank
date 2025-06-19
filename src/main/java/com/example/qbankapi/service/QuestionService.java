package com.example.qbankapi.service;

import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.AddQuestionRequestDto;
import com.example.qbankapi.dto.QuestionResponseDto;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;
    private final SubjectDao subjectDao;

    @Transactional(readOnly = true)
    public List<QuestionResponseDto> getAllInDto() {
        return questionDao.findAll()
                .stream()
                .map(question -> QuestionResponseDto.builder()
                        .id(question.getId())
                        .text(question.getText())
                        .options(question.getOptions())
                        .correctAnswer(question.getCorrectAnswer())
                        .complexity(question.getComplexity())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addAll(List<AddQuestionRequestDto> addQuestionRequestDtoList) {
        addQuestionRequestDtoList.stream()
                .forEach(question -> {
                    Subject subject = subjectDao.findById(question.getSubjectId())
                            .orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));

                    Question newQuestion = Question.builder()
                            .text(question.getText())
                            .correctAnswer(question.getCorrectAnswer())
                            .complexity(question.getComplexity())
                            .subject(subject)
                            .build();
                    newQuestion.getOptions().add(question.getOptionA());
                    newQuestion.getOptions().add(question.getOptionB());
                    newQuestion.getOptions().add(question.getOptionC());
                    newQuestion.getOptions().add(question.getOptionD());

                    subject.getQuestions().add(newQuestion);

                    subjectDao.update(subject);
                    questionDao.save(newQuestion);
                });
    }

}
