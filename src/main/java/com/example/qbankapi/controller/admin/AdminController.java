package com.example.qbankapi.controller.admin;

import com.example.qbankapi.dto.model.SubjectDto;
import com.example.qbankapi.dto.request.AddSubjectRequestDto;
import com.example.qbankapi.dto.request.UpdateSubjectRequestDto;
import com.example.qbankapi.exception.base.impl.SubjectAlreadyExistsException;
import com.example.qbankapi.exception.base.impl.SubjectNotFoundException;
import com.example.qbankapi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SubjectService subjectService;
//    private final QuestionService questionService;
//    private final ExamService examService;
//    private final BaseUserService baseUserService;

    @GetMapping("/home")
    public String getDashboardPage() {
        log.info("Rendering admin dashboard page");
        return "admin/dashboard";
    }

    @GetMapping("/manage/subjects")
    public String getManageSubjectsPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());

        log.info("Rendering subject-manage page");
        return "admin/subject-manage";
    }

    @GetMapping("/manage/subjects/add")
    public String getAddSubjectPage(Model model) {
        model.addAttribute("addSubjectRequest", new AddSubjectRequestDto());

        log.info("Rendering subject-add page");
        return "admin/subject-add";
    }

    @PostMapping("/manage/subjects/save")
    public String addSubject(
            @Valid @ModelAttribute("addSubjectRequest") AddSubjectRequestDto addSubjectRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
            return "admin/subject-add";
        }

        try {
            subjectService.addSubject(addSubjectRequest);
            log.debug("Subject added with name: {}", addSubjectRequest.getName());

            redirectAttributes.addFlashAttribute("message", "Subject added successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        } catch (SubjectAlreadyExistsException ex) {
            log.warn("Subject with name: {} already exists.", addSubjectRequest.getName(), ex);

            bindingResult.rejectValue("name", "SubjectName.Invalid", "Subject name already exists.");
            return "admin/subject-add";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not created.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        }
    }

    @GetMapping("/manage/subjects/{id}/edit")
    public String getEditSubjectPage(
            @PathVariable("id") Long subjectId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UpdateSubjectRequestDto updateSubjectRequestDto = new UpdateSubjectRequestDto();

            SubjectDto subjectDto = subjectService.getSubjectDtoById(subjectId);
            updateSubjectRequestDto.setId(subjectDto.getId());
            updateSubjectRequestDto.setName(subjectDto.getName());
            updateSubjectRequestDto.setDescription(subjectDto.getDescription());

            model.addAttribute("updateSubjectRequest", updateSubjectRequestDto);

            log.info("Rendering subject-edit page");
            return "admin/subject-edit";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id: {}", subjectId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        }
    }

//    @PostMapping("/manage/subjects/edit")
//    public String editSubject(
//            @Valid @ModelAttribute("updateSubjectRequest") UpdateSubjectRequestDto updateSubjectRequest,
//            BindingResult bindingResult,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//            return "admin/subject-edit";
//        }
//
//        try {
//            subjectService.updateSubject(updateSubjectRequest);
//            log.debug("Subject created with name: {}", updateSubjectRequest.getName());
//
//            redirectAttributes.addFlashAttribute("message", "Subject updated successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/subjects");
//            return "redirect:/admin/manage/subjects";
//        } catch (SubjectNotFoundException ex) {
//            log.error("Subject not found with id: {}", updateSubjectRequest.getId(), ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/subjects");
//            return "redirect:/admin/manage/subjects";
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Critical error: DataIntegrityViolation", ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not updated.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/subjects");
//            return "redirect:/admin/manage/subjects";
//        }
//    }
//
//    @GetMapping("/manage/questions")
//    public String getManageQuestionsPage(
//            @Valid @ModelAttribute("filter") QuestionFilterDto questionFilterDto,
//            Model model,
//            BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//            return "admin/question-manage";
//        }
//
//        QuestionPageViewDto questionViewPage = questionService.getFilteredQuestions(questionFilterDto);
//
//        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//        model.addAttribute("filter", questionFilterDto);
//
//        model.addAttribute("questions", questionViewPage.getQuestions());
//        model.addAttribute("pageNumber", questionViewPage.getPage());
//        model.addAttribute("pageSize", questionViewPage.getPageSize());
//        model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());
//
//        log.info("Rendering question-manage page");
//        return "admin/question-manage";
//    }
//
//    @GetMapping("/manage/questions/add")
//    public String getAddQuestionPage(Model model) {
//        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//        model.addAttribute("addQuestionRequest", new AddQuestionRequestDto());
//
//        log.info("Rendering question-add page");
//        return "admin/question-add";
//    }
//
//    @PostMapping("/manage/questions/save")
//    public String addQuestion(
//            @Valid @ModelAttribute("addQuestionRequest") AddQuestionRequestDto addQuestionRequest,
//            BindingResult bindingResult,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//            return "admin/question-add";
//        }
//
//        try {
//            questionService.createQuestion(addQuestionRequest);
//
//            redirectAttributes.addFlashAttribute("message", "Question created successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (SubjectNotFoundException ex) {
//            log.error("Subject not found with id: {}", addQuestionRequest.getSubjectId(), ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Critical error: DataIntegrityViolation", ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not created.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        }
//    }
//
//    @GetMapping("/manage/questions/{id}/edit")
//    public String getEditQuestionPage(
//            @PathVariable("id") Long questionId,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            UpdateQuestionRequestDto updateQuestionRequest = new UpdateQuestionRequestDto();
//
//            QuestionDto questionDto = questionService.getQuestionDtoById(questionId);
//
//            updateQuestionRequest.setId(questionDto.getId());
//            updateQuestionRequest.setText(questionDto.getText());
//            updateQuestionRequest.setOptionA(questionDto.getOptions().get(0));
//            updateQuestionRequest.setOptionB(questionDto.getOptions().get(1));
//            updateQuestionRequest.setOptionC(questionDto.getOptions().get(2));
//            updateQuestionRequest.setOptionD(questionDto.getOptions().get(3));
//            updateQuestionRequest.setCorrectAnswer(questionDto.getCorrectAnswer());
//            updateQuestionRequest.setComplexity(questionDto.getComplexity());
//            updateQuestionRequest.setMarks(questionDto.getMarks());
//            updateQuestionRequest.setSubjectId(questionDto.getSubjectId());
//
//            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//            model.addAttribute("updateQuestionRequest", updateQuestionRequest);
//
//            log.info("Rendering question-edit page");
//            return "admin/question-edit";
//        } catch (QuestionNotFoundException ex) {
//            log.error("Question not found with id: {}", questionId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        }
//    }
//
//    @PostMapping("/manage/questions/edit")
//    public String editQuestion(
//            @Valid @ModelAttribute("updateQuestionRequest") UpdateQuestionRequestDto updateQuestionRequest,
//            BindingResult bindingResult,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//            return "admin/question-edit";
//        }
//
//        try {
//            questionService.updateQuestion(updateQuestionRequest);
//            log.debug("Question updated with id: {}", updateQuestionRequest.getId());
//
//            redirectAttributes.addFlashAttribute("message", "Question updated successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (QuestionNotFoundException ex) {
//            log.error("Question not found with id: {}", updateQuestionRequest.getId(), ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (SubjectNotFoundException ex) {
//            log.error("Subject not found with id: {}", updateQuestionRequest.getSubjectId(), ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Critical error: DataIntegrityViolation", ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not updated.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        }
//    }
//
//    @GetMapping("/manage/questions/{id}/delete")
//    public String removeQuestion(
//            @PathVariable("id") Long questionId,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            questionService.deactivateQuestion(questionId);
//
//            log.debug("Question deactivated with id: {}", questionId);
//
//            redirectAttributes.addFlashAttribute("message", "Question deleted successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (QuestionNotFoundException ex) {
//            log.error("Question not found with id: {}", questionId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Critical error: DataIntegrityViolation", ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not deleted.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        }
//    }
//
//    @GetMapping("/manage/exams")
//    public String getManageExamsPage(
//            @Valid @ModelAttribute("filter") ExamFilterDto examFilterDto,
//            Model model,
//            BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            return "admin/exam-manage";
//        }
//
//        ExamPageViewDto examViewPageDto = examService.getFilteredExams(examFilterDto);
//
//        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//        model.addAttribute("filter", examFilterDto);
//
//        model.addAttribute("exams", examViewPageDto.getExams());
//        model.addAttribute("pageNumber", examViewPageDto.getPage());
//        model.addAttribute("pageSize", examViewPageDto.getPageSize());
//        model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());
//
//        log.info("Rendering exam-manage page");
//        return "admin/exam-manage";
//    }
//
//    @GetMapping("/manage/exams/create")
//    public String getCreateExamPage(Model model) {
//        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//        model.addAttribute("createExamRequest", new CreateExamRequestDto());
//
//        log.info("Rendering exam-add page");
//        return "admin/exam-add";
//    }
//
//    @PostMapping("/manage/exams/save")
//    public String createExam(
//            @Valid @ModelAttribute("createExamRequest") CreateExamRequestDto createExamRequestDto,
//            BindingResult bindingResult,
//            HttpSession session,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            return "admin/exam-add";
//        }
//
//        if (!createExamRequestDto.getEnrollmentStartDate().isBefore(createExamRequestDto.getEnrollmentEndDate())) {
//            log.warn("Validation failed: enrollmentStartDate is not before enrollmentEndDate.");
//
//            model.addAttribute("error", "Enrollment start date must be before enrollment end date.");
//            return "admin/exam-add";
//        }
//
//        if (!createExamRequestDto.getExamStartDate().isBefore(createExamRequestDto.getExamEndDate())) {
//            log.warn("Validation failed: examStartDate is not before examEndDate.");
//
//            model.addAttribute("error", "Exam start date must be before exam end date.");
//            return "admin/exam-add";
//        }
//
//        if (!createExamRequestDto.getEnrollmentEndDate().isBefore(createExamRequestDto.getExamStartDate())) {
//            log.warn("Validation failed: enrollmentEndDate is not before examStartDate.");
//
//            model.addAttribute("error", "Enrollment must end before the exam starts.");
//            return "admin/exam-add";
//        }
//
//        try {
//            examService.createExam(createExamRequestDto, (Long) session.getAttribute(USER_ID));
//            log.info("Exam created with description: {}", createExamRequestDto.getDescription());
//
//            redirectAttributes.addFlashAttribute("message", "Exam created successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/exams");
//            return "redirect:/admin/manage/exams";
//        } catch (InsufficientQuestionsException ex) {
//            log.error("Critical error: InSufficientQuestions", ex);
//
//            redirectAttributes.addFlashAttribute("message", "Unable to complete the operation due to insufficient available questions");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/exams");
//            return "redirect:/admin/manage/exams";
//        }
//    }
//
//    @GetMapping("/manage/users")
//    public String getManageUsersPage(
//            @Valid @ModelAttribute("filter") BaseUserFilterDto userFilterDto,
//            Model model,
//            BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            return "admin/user-manage";
//        }
//
//        BaseUserPageViewDto baseUserViewPageDto = baseUserService.getFilteredUsers(userFilterDto);
//        model.addAttribute("filter", userFilterDto);
//
//        model.addAttribute("baseUsers", baseUserViewPageDto.getBaseUsers());
//        model.addAttribute("pageNumber", baseUserViewPageDto.getPage());
//        model.addAttribute("pageSize", baseUserViewPageDto.getPageSize());
//        model.addAttribute("hasNextPage", baseUserViewPageDto.getHasNextPage());
//
//        log.info("Rendering user-manage page");
//        return "admin/user-manage";
//    }
//
//    @GetMapping("/manage/users/{id}/activate")
//    public String activateUser(
//            @PathVariable("id") Long userId,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            baseUserService.activateUser(userId);
//            log.debug("BaseUser activated with id: {}", userId);
//
//            redirectAttributes.addFlashAttribute("message", "User activated successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/users");
//            return "redirect:/admin/manage/users";
//        } catch (BaseUserNotFoundException ex) {
//            log.error("BaseUser not found with id: {}", userId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. BaseUser not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/users");
//            return "redirect:/admin/manage/users";
//        }
//    }
//
//    @GetMapping("/manage/users/{id}/inactivate")
//    public String inactivateUser(
//            @PathVariable("id") Long userId,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            baseUserService.inactivateUser(userId);
//            log.debug("BaseUser inactivated with id: {}", userId);
//
//            redirectAttributes.addFlashAttribute("message", "User inactivated successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/users");
//            return "redirect:/admin/manage/users";
//        } catch (BaseUserNotFoundException ex) {
//            log.error("BaseUser not found with id: {}", userId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. BaseUser not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /admin/manage/users");
//            return "redirect:/admin/manage/users";
//        }
//    }

}
