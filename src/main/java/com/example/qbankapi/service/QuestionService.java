package com.example.qbankapi.service;

import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.model.QuestionDto;
import com.example.qbankapi.dto.model.QuestionFilterDto;
import com.example.qbankapi.dto.model.QuestionViewPageDto;
import com.example.qbankapi.dto.model.UpdateQuestionDto;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.request.UpdateQuestionRequestDto;
import com.example.qbankapi.dto.response.QuestionResponseDto;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.QuestionNotFoundException;
import com.example.qbankapi.exception.SubjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;
    private final SubjectDao subjectDao;

    @Transactional
    public void createQuestion(AddQuestionRequestDto addQuestionRequest) {
        Subject subject = subjectDao.findById(addQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(String.format("Subject not found with id: %d", addQuestionRequest.getSubjectId())));

        Question question = new Question();
        question.setText(addQuestionRequest.getText());
        question.setOptions(List.of(
                addQuestionRequest.getOptionA(),
                addQuestionRequest.getOptionB(),
                addQuestionRequest.getOptionC(),
                addQuestionRequest.getOptionD()
        ));
        question.setCorrectAnswer(addQuestionRequest.getCorrectAnswer());
        question.setComplexity(addQuestionRequest.getComplexity());
        question.setMarks(addQuestionRequest.getMarks());
        question.setIsActive(Boolean.TRUE);
        question.setSubject(subject);
        questionDao.save(question);

        subject.getQuestions().add(question);
        subjectDao.update(subject);
    }

    @Transactional(readOnly = true)
    public QuestionViewPageDto getFilteredQuestions(QuestionFilterDto questionFilterDto) {
        if (questionFilterDto.getSubjectId() == null || questionFilterDto.getSubjectId() <= 0)
            questionFilterDto.setSubjectId(0L);
        if (questionFilterDto.getComplexity() == null || questionFilterDto.getComplexity().isBlank())
            questionFilterDto.setComplexity("ALL");
        if (questionFilterDto.getMarks() == null || questionFilterDto.getMarks() <= 0) questionFilterDto.setMarks(0L);
        if (questionFilterDto.getSortBy() == null || questionFilterDto.getSortBy().isBlank())
            questionFilterDto.setSortBy("id");
        if (questionFilterDto.getSortOrder() == null || questionFilterDto.getSortOrder().isBlank())
            questionFilterDto.setSortOrder("ASC");
        if (questionFilterDto.getPageSize() == null || questionFilterDto.getPageSize() <= 0)
            questionFilterDto.setPageSize(10);
        if (questionFilterDto.getPageSize() != 5 && questionFilterDto.getPageSize() != 10 && questionFilterDto.getPageSize() != 20)
            questionFilterDto.setPageSize(10);
        if (questionFilterDto.getPage() == null || questionFilterDto.getPage() < 0) questionFilterDto.setPage(0);

        System.out.println("subjectId = " + Optional.ofNullable(questionFilterDto.getSubjectId()));
        System.out.println("complexity = " + Optional.ofNullable(questionFilterDto.getComplexity()));
        System.out.println("marks = " + Optional.ofNullable(questionFilterDto.getMarks()));
        System.out.println("sortBy = " + Optional.ofNullable(questionFilterDto.getSortBy()));
        System.out.println("sortOrder = " + Optional.ofNullable(questionFilterDto.getSortOrder()));
        System.out.println("size = " + Optional.ofNullable(questionFilterDto.getPageSize()));
        System.out.println("page = " + Optional.ofNullable(questionFilterDto.getPage()));

        return questionDao.findFilteredQuestions(questionFilterDto);
    }

    @Transactional(readOnly = true)
    public UpdateQuestionDto getQuestionDtoById(Long id) {
        Question question = questionDao.findById(id).orElseThrow(() -> new QuestionNotFoundException(String.format("Question not found with id: %d",id)));
        return UpdateQuestionDto.builder()
                .id(question.getId())
                .text(question.getText())
                .options(question.getOptions())
                .correctAnswer(question.getCorrectAnswer())
                .complexity(question.getComplexity())
                .marks(question.getMarks())
                .subjectId(question.getSubject().getId())
                .build();
    }

    @Transactional
    public void updateQuestion(UpdateQuestionRequestDto updateQuestionRequest) {
        Question question = questionDao.findById(updateQuestionRequest.getId())
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + updateQuestionRequest.getId()));

        Subject subject = subjectDao.findById(updateQuestionRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + updateQuestionRequest.getSubjectId()));

        question.setText(updateQuestionRequest.getText());
        question.setOptions(new ArrayList<>(List.of(
                updateQuestionRequest.getOptionA(),
                updateQuestionRequest.getOptionB(),
                updateQuestionRequest.getOptionC(),
                updateQuestionRequest.getOptionD()
        )));
        question.setCorrectAnswer(updateQuestionRequest.getCorrectAnswer());
        question.setComplexity(updateQuestionRequest.getComplexity());
        question.setMarks(updateQuestionRequest.getMarks());
        question.setSubject(subject);
        questionDao.update(question);
    }

    @Transactional
    public void deactivateQuestion(Long id) {
        Question question = questionDao.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + id));
        question.setIsActive(Boolean.FALSE);
        questionDao.update(question);
    }

//    @Transactional(readOnly = true)
//    public List<QuestionResponseDto> getAllInDto() {
//        return questionDao.findAll()
//                .stream()
//                .map(question -> QuestionResponseDto.builder()
//                        .id(question.getId())
//                        .text(question.getText())
//                        .options(question.getOptions())
//                        .correctAnswer(question.getCorrectAnswer())
//                        .complexity(question.getComplexity())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void addAll(List<AddQuestionRequestDto> addQuestionRequestDtoList) {
//        addQuestionRequestDtoList.stream()
//                .forEach(question -> {
//                    Subject subject = subjectDao.findById(question.getSubjectId())
//                            .orElseThrow(() -> new EntityNotFoundException("Subject Not Found"));
//
//                    Question newQuestion = Question.builder()
//                            .text(question.getText())
//                            .correctAnswer(question.getCorrectAnswer())
//                            .complexity(question.getComplexity())
//                            .subject(subject)
//                            .build();
//                    newQuestion.getOptions().add(question.getOptionA());
//                    newQuestion.getOptions().add(question.getOptionB());
//                    newQuestion.getOptions().add(question.getOptionC());
//                    newQuestion.getOptions().add(question.getOptionD());
//
//                    subject.getQuestions().add(newQuestion);
//
//                    subjectDao.update(subject);
//                    questionDao.save(newQuestion);
//                });
//    }

}
