package com.example.qbankapi.controller;

import com.example.qbankapi.DTO.ExamDTO;
import com.example.qbankapi.DTO.QuestionDTO;
import com.example.qbankapi.DTO.SubjectDTO;
import com.example.qbankapi.DTO.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/subject")
    public List<SubjectDTO> getAllSubject() {
        return List.of();
    }

    @GetMapping("/subject/{id}")
    public SubjectDTO getSubject(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/subject")
    public SubjectDTO addSubject(@RequestBody SubjectDTO subjectDTO) {
        return null;
    }

    @GetMapping("/question")
    public List<QuestionDTO> getAllQuestion() {
        return List.of();
    }

    @GetMapping("/question")
    public List<QuestionDTO> getAllQuestionBySubject(@RequestParam("subject") String subject) {
        return null;
    }

    @GetMapping("/question/{id}")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/question")
    public QuestionDTO addSubject(@RequestBody QuestionDTO questionDTO) {
        return null;
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/user/all")
    public List<UserDTO> addUsers(@RequestBody List<UserDTO> userDto) {
        return null;
    }

    @PostMapping("/exam/create")
    public ExamDTO createExam(@RequestBody ExamDTO examDTO) {
        return null;
    }

}
