package com.example.qbankapi.controller.admin;

import com.example.qbankapi.dto.model.QuestionFilterDto;
import com.example.qbankapi.dto.model.QuestionViewPageDto;
import com.example.qbankapi.dto.model.SubjectDto;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.request.AddSubjectRequestDto;
import com.example.qbankapi.dto.request.UpdateSubjectRequestDto;
import com.example.qbankapi.exception.SubjectAlreadyExistsException;
import com.example.qbankapi.exception.SubjectNotFoundException;
import com.example.qbankapi.service.QuestionService;
import com.example.qbankapi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SubjectService subjectService;
    private final QuestionService questionService;

    @GetMapping("/home")
    public String getDashboardPage() {
        log.info("Rendering admin dashboard page");
        return "/admin/dashboard";
    }

    @GetMapping("/manage/subjects")
    public String getManageSubjectsPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectDtoList());

        log.info("Rendering subject-manage page");
        return "/admin/subject-manage";
    }

    @GetMapping("/manage/subjects/add")
    public String getAddSubjectPage(Model model) {
        model.addAttribute("addSubjectRequest", new AddSubjectRequestDto());

        log.info("Rendering subject-add page");
        return "/admin/subject-add";
    }

    @PostMapping("/manage/subjects/save")
    public String addSubject(
            @Valid @ModelAttribute("addSubjectRequest") AddSubjectRequestDto addSubjectRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "/admin/subject-add";
        }

        try {
            subjectService.createSubject(addSubjectRequest);
        } catch (SubjectAlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            return "/admin/subject-add";
        }

        model.addAttribute("message", "Subject added successfully");
        return "/admin/success";
    }

    @GetMapping("/manage/subjects/{id}/edit")
    public String getEditSubjectPage(@PathVariable("id") Long id, Model model) {
        try {
            UpdateSubjectRequestDto updateSubjectRequestDto = new UpdateSubjectRequestDto();
            SubjectDto subjectDto = subjectService.getSubjectDtoById(id);
            updateSubjectRequestDto.setId(subjectDto.getId());
            updateSubjectRequestDto.setName(subjectDto.getName());
            updateSubjectRequestDto.setDescription(subjectDto.getDescription());

            model.addAttribute("updateSubjectRequest", updateSubjectRequestDto);

            log.info("Rendering subject-edit page");
            return "/admin/subject-edit";
        } catch (SubjectNotFoundException ex) {

            model.addAttribute("message", ex.getMessage());

            return "/admin/error";
        }
    }

    @PostMapping("/manage/subjects/edit")
    public String addSubject(
            @Valid @ModelAttribute("updateSubjectRequest") UpdateSubjectRequestDto updateSubjectRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "/admin/subject-edit";
        }

        try {
            subjectService.updateSubject(updateSubjectRequest);
        } catch (SubjectNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
            return "/admin/error";
        }

        model.addAttribute("message", "Subject updated successfully");
        return "/admin/success";
    }

    @GetMapping("/manage/questions")
    public String getManageQuestionsPage(
            @ModelAttribute("filter") QuestionFilterDto questionFilterDto,
            Model model
    ) {

//        if (questionFilterDto.getSubjectId() != null && questionFilterDto.getSubjectId() == -1L)
//            questionFilterDto.setSubjectId(null);
//        if (questionFilterDto.getMarks() != null && questionFilterDto.getMarks() == -1)
//            questionFilterDto.setMarks(null);
//        if ("*".equals(questionFilterDto.getOptionRegex())) questionFilterDto.setOptionRegex(null);
//        if (questionFilterDto.getQuestionRegex() != null && questionFilterDto.getQuestionRegex().isBlank())
//            questionFilterDto.setQuestionRegex(null);
//        if (questionFilterDto.getDifficulty() != null && questionFilterDto.getDifficulty().isBlank())
//            questionFilterDto.setDifficulty(null);
//        if (questionFilterDto.getSortBy() == null || questionFilterDto.getSortBy().isBlank())
//            questionFilterDto.setSortBy("text");

        QuestionViewPageDto questionViewPage = questionService.getFilteredQuestions(questionFilterDto);

        model.addAttribute("subjects", subjectService.getSubjectDtoList());
        model.addAttribute("filter", questionFilterDto);

        model.addAttribute("questions", questionViewPage.getQuestions());
        model.addAttribute("pageNumber", questionViewPage.getPage());
        model.addAttribute("pageSize", questionViewPage.getPageSize());
        model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());
        return "/admin/question-manage";
    }


    @GetMapping("/manage/questions/add")
    public String getAddQuestionPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectDtoList());
        model.addAttribute("addQuestionRequest", new AddQuestionRequestDto());
        return "/admin/question-add";
    }

    @PostMapping("/manage/questions/save")
    public String addQuestion(
            @Valid @ModelAttribute("addSubjectRequest") AddQuestionRequestDto addQuestionRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "/admin/question-add";
        }

        try {
            questionService.createQuestion(addQuestionRequest);
        } catch (SubjectNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
            return "/admin/error";
        }

        model.addAttribute("message", "Question added successfully");
        return "/admin/success";
    }


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
