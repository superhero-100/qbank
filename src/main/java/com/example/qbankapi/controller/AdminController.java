package com.example.qbankapi.controller;

import com.example.qbankapi.dto.*;
import com.example.qbankapi.service.ExamService;
import com.example.qbankapi.service.QuestionService;
import com.example.qbankapi.service.SubjectService;
import com.example.qbankapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SubjectService subjectService;
    private final QuestionService questionService;
    private final UserService userService;
    private final ExamService examService;

    @GetMapping(value = "/subject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubjectResponseDto>> getAllSubject() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectService.getAll()
                        .stream()
                        .map(subject -> SubjectResponseDto.builder()
                                .id(subject.getId())
                                .name(subject.getName())
                                .build()
                        )
                        .collect(Collectors.toList())
                );
    }

    @PostMapping(value = "/subject", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addSubjects(
            @Valid @RequestBody List<@Valid AddSubjectRequestDto> addSubjectRequestDtoList
    ) {
        subjectService.addAll(
                addSubjectRequestDtoList.stream()
                        .map(subjectDto -> subjectDto.getName())
                        .collect(Collectors.toList())
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/question")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questionService.getAll()
                        .stream()
                        .map(question -> QuestionResponseDto.builder()
                                .id(question.getId())
                                .text(question.getText())
                                .options(question.getOptions())
                                .correctAnswer(question.getCorrectAnswer())
                                .complexity(question.getComplexity())
                                .build())
                        .collect(Collectors.toList()));
    }

    @PostMapping("/question")
    public ResponseEntity<Void> addQuestions(
            @Valid @RequestBody List<@Valid AddQuestionRequestDto> addQuestionRequestDtoList
    ) {
        questionService.addAll(addQuestionRequestDtoList);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAll().stream().map(user -> UserResponseDto.builder().username(user.getUsername()).password(user.getPassword()).build()).collect(Collectors.toList()));
    }

    @PostMapping("/user")
    public ResponseEntity<Void> addUsers(
            @Valid @RequestBody List<@Valid AddUserRequestDto> addUserRequestDtoList
    ) {
        userService.addAll(addUserRequestDtoList);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/exam/create")
    public ResponseEntity<Void> createExam(
            @Valid @RequestBody CreateExamRequestDto examDTO
    ) {
        examService.createExam(examDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}
