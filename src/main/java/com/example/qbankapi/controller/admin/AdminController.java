package com.example.qbankapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/home")
    @ResponseBody
    public String hello(){
        return "hello";
    }

//    private final SubjectService subjectService;
//    private final QuestionService questionService;
//    private final UserService userService;
//    private final ExamService examService;

//    @GetMapping(value = "/subject", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<SubjectResponseDto>> getAllSubject() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(subjectService.getAll()
//                        .stream()
//                        .map(subject -> SubjectResponseDto.builder()
//                                .id(subject.getId())
//                                .name(subject.getName())
//                                .build()
//                        )
//                        .collect(Collectors.toList())
//                );
//    }
//
//    @PostMapping(value = "/subject", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> addSubjects(
//            @RequestBody List<AddSubjectRequestDto> addSubjectRequestDtoList
//    ) {
//        subjectService.addAll(
//                addSubjectRequestDtoList.stream()
//                        .map(subjectDto -> subjectDto.getName())
//                        .collect(Collectors.toList())
//        );
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .build();
//    }
//
//    @GetMapping("/question")
//    public ResponseEntity<List<QuestionResponseDto>> getAllQuestion() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(questionService.getAllInDto());
//    }
//
//    @PostMapping("/question")
//    public ResponseEntity<Void> addQuestions(
//            @RequestBody List<AddQuestionRequestDto> addQuestionRequestDtoList
//    ) {
//        questionService.addAll(addQuestionRequestDtoList);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .build();
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<List<UserResponseDto>> getAllUser() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.getAll().stream().map(user -> UserResponseDto.builder().username(user.getUsername()).password(user.getPassword()).build()).collect(Collectors.toList()));
//    }
//
//    @PostMapping("/user")
//    public ResponseEntity<Void> addUsers(
//            @RequestBody List<AddUserRequestDto> addUserRequestDtoList
//    ) {
//        userService.addAll(addUserRequestDtoList);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .build();
//    }
//
//    @PostMapping("/exam/create")
//    public ResponseEntity<Void> createExam(
//            @RequestBody CreateExamRequestDto examDTO
//    ) {
//        examService.createExam(examDTO);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .build();
//    }

}
