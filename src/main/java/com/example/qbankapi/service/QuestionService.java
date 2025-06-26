package com.example.qbankapi.service;

import com.example.qbankapi.dao.QuestionDao;
import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.dto.model.QuestionFilterDto;
import com.example.qbankapi.dto.model.QuestionViewPageDto;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.response.QuestionResponseDto;
import com.example.qbankapi.entity.Question;
import com.example.qbankapi.entity.Subject;
import com.example.qbankapi.exception.SubjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new SubjectNotFoundException(
                        String.format("Subject not found with id: %d", addQuestionRequest.getSubjectId()))
                );

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
        question.setSubject(subject);
        questionDao.save(question);

        subject.getQuestions().add(question);
        subjectDao.update(subject);
    }

    public QuestionViewPageDto getFilteredQuestions(QuestionFilterDto questionFilterDto) {

        System.out.println("subjectId = " + Optional.ofNullable(questionFilterDto.getSubjectId()));
        System.out.println("difficulty = " + Optional.ofNullable(questionFilterDto.getDifficulty()));
        System.out.println("marks = " + Optional.ofNullable(questionFilterDto.getMarks()));
        System.out.println("questionRegex = " + Optional.ofNullable(questionFilterDto.getQuestionRegex()));
        System.out.println("optionRegex = " + Optional.ofNullable(questionFilterDto.getOptionRegex()));
        System.out.println("sortBy = " + Optional.ofNullable(questionFilterDto.getSortBy()));
        System.out.println("page = " + Optional.ofNullable(questionFilterDto.getPage()));
        System.out.println("size = " + Optional.ofNullable(questionFilterDto.getPageSize()));

        QuestionViewPageDto questionViewPageDto = new QuestionViewPageDto();
        questionViewPageDto.setQuestions(List.of());
        questionViewPageDto.setPage(questionFilterDto.getPage());
        questionViewPageDto.setHasNextPage(Boolean.FALSE);
        questionViewPageDto.setPageSize(questionFilterDto.getPageSize());
        return questionViewPageDto;
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
