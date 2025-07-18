package com.example.qbankapi.controller.instructor;

import com.example.qbankapi.dto.model.*;
import com.example.qbankapi.dto.request.AddQuestionRequestDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.request.UpdateQuestionRequestDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.dto.view.QuestionAnalyticsViewDto;
import com.example.qbankapi.dto.view.QuestionPageViewDto;
import com.example.qbankapi.exception.base.impl.*;
import com.example.qbankapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorController {

    private final SubjectService subjectService;
    private final QuestionService questionService;
    private final ExamService examService;
    private final InstructorUserService instructorUserService;
    private final BaseUserService baseUserService;

    @GetMapping("/home")
    public String getDashboardPage() {
        log.info("Rendering instructor dashboard page");
        return "instructor/dashboard";
    }

    @GetMapping("/view/subjects")
    public String getViewSubjectsPage(
            HttpSession httpSession,
            Model model
    ) {
        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor((Long) httpSession.getAttribute(USER_ID)));

        log.info("Rendering subject-view page");
        return "instructor/subject-view";
    }

    @GetMapping("/manage/questions")
    public String getManageQuestionsPage(
            @Valid @ModelAttribute("filter") AllQuestionFilterDto allQuestionFilterDto,
            HttpSession httpSession,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation Errors: {}", bindingResult.getAllErrors());
            return "instructor/question-manage";
        }

        Long instructorId = (Long) httpSession.getAttribute(USER_ID);

        QuestionPageViewDto questionViewPage = questionService.getFilteredQuestionsForInstructor(allQuestionFilterDto, instructorId);

        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor(instructorId));
        model.addAttribute("filter", allQuestionFilterDto);

        model.addAttribute("questions", questionViewPage.getQuestions());
        model.addAttribute("pageNumber", questionViewPage.getPage());
        model.addAttribute("pageSize", questionViewPage.getPageSize());
        model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());

        log.info("Rendering question-manage page");
        return "instructor/question-manage";
    }

    @GetMapping("/manage/questions/{questionId}/analytics")
    public String getQuestionAnalyticsPage(
            @PathVariable("questionId") Long questionId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            QuestionAnalyticsViewDto questionAnalyticsViewDto = questionService.getQuestionAnalytics(questionId, baseUserService.findByIdAndGetZoneId((Long) httpSession.getAttribute(USER_ID)));

            model.addAttribute("questionAnalytics", questionAnalyticsViewDto);

            log.info("Rendering question-analytics page");
            return "instructor/question-analytics";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id: {}", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/manage/questions");
            return "redirect:/instructor/manage/questions";
        }
    }

    @GetMapping("/manage/questions/add")
    public String getAddQuestionPage(
            HttpSession httpSession,
            Model model
    ) {
        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor((Long) httpSession.getAttribute(USER_ID)));
        model.addAttribute("addQuestionRequest", new AddQuestionRequestDto());

        log.info("Rendering question-add page");
        return "instructor/question-add";
    }

    @PostMapping("/manage/questions/save")
    public String addQuestion(
            @Valid @ModelAttribute("addQuestionRequest") AddQuestionRequestDto addQuestionRequest,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            return "admin/question-add";
        }

        try {
            Long instructorId = (Long) httpSession.getAttribute(USER_ID);

            questionService.instructorAddQuestion(addQuestionRequest, instructorId);

            redirectAttributes.addFlashAttribute("message", "Question added successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /instructor/manage/questions");
            return "redirect:/instructor/manage/questions";
        } catch (SubjectNotFoundException | AccessDeniedException ex) {
            log.error("Subject not found or not assigned with id: {}", addQuestionRequest.getSubjectId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found or not assigned.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/manage/questions");
            return "redirect:/instructor/manage/questions";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not created.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/manage/questions");
            return "redirect:/instructor/manage/questions";
        }
    }

    @GetMapping("/manage/questions/{questionId}/edit")
    public String getEditQuestionPage(
            @PathVariable("questionId") Long questionId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            questionService.isQuestionSubjectAssignedToInstructor(questionId, (Long) httpSession.getAttribute(USER_ID));

            UpdateQuestionRequestDto updateQuestionRequest = new UpdateQuestionRequestDto();

            QuestionDto questionDto = questionService.getQuestionDtoById(questionId);

            updateQuestionRequest.setId(questionDto.getId());
            updateQuestionRequest.setText(questionDto.getText());
            updateQuestionRequest.setOptionA(questionDto.getOptions().get(0));
            updateQuestionRequest.setOptionB(questionDto.getOptions().get(1));
            updateQuestionRequest.setOptionC(questionDto.getOptions().get(2));
            updateQuestionRequest.setOptionD(questionDto.getOptions().get(3));
            updateQuestionRequest.setCorrectAnswer(questionDto.getCorrectAnswer());
            updateQuestionRequest.setComplexity(questionDto.getComplexity());
            updateQuestionRequest.setMarks(questionDto.getMarks());
            updateQuestionRequest.setSubjectId(questionDto.getSubjectId());

            model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor((Long) httpSession.getAttribute(USER_ID)));
            model.addAttribute("updateQuestionRequest", updateQuestionRequest);

            log.info("Rendering question-edit page");
            return "instructor/question-edit";
        } catch (QuestionNotFoundException | AccessDeniedException ex) {
            log.error("Question not found with id: {}", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/manage/questions");
            return "redirect:/instructor/manage/questions";
        }
    }

    @PostMapping("/manage/questions/edit")
    public String editQuestion(
            @Valid @ModelAttribute("updateQuestionRequest") UpdateQuestionRequestDto updateQuestionRequest,
            HttpSession httpSession,
            Model model,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            return "admin/question-edit";
        }

        try {
            questionService.instructorUpdateQuestion(updateQuestionRequest, (Long) httpSession.getAttribute(USER_ID));
            log.debug("Question updated with id: {}", updateQuestionRequest.getId());

            redirectAttributes.addFlashAttribute("message", "Question updated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (QuestionNotFoundException | AccessDeniedException ex) {
            log.error("Question not found with id: {}", updateQuestionRequest.getId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id: {}", updateQuestionRequest.getSubjectId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not updated.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @GetMapping("/manage/exams")
    public String getManageExamsPage(
            @Valid @ModelAttribute("filter") AllExamFilterDto allExamFilterDto,
            HttpSession httpSession,
            Model model,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            return "instructor/exam-manage";
        }

        long instructorUserId = (Long) httpSession.getAttribute(USER_ID);

        ExamPageViewDto examViewPageDto = examService.getFilteredExamsForInstructor(allExamFilterDto, instructorUserId);

        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor(instructorUserId));
        model.addAttribute("filter", allExamFilterDto);

        model.addAttribute("exams", examViewPageDto.getExams());
        model.addAttribute("pageNumber", examViewPageDto.getPage());
        model.addAttribute("pageSize", examViewPageDto.getPageSize());
        model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());

        log.info("Rendering exam-manage page");
        return "instructor/exam-manage";
    }

    @GetMapping("/manage/exams/create")
    public String getCreateExamPage(
            HttpSession httpSession,
            Model model
    ) {
        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor((Long) httpSession.getAttribute(USER_ID)));
        model.addAttribute("createExamRequest", new CreateExamRequestDto());

        log.info("Rendering exam-add page");
        return "instructor/exam-add";
    }

    @PostMapping("/manage/exams/save")
    public String createExam(
            @Valid @ModelAttribute("createExamRequest") CreateExamRequestDto createExamRequestDto,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            return "instructor/exam-add";
        }

//        if (!createExamRequestDto.getEnrollmentStartDate().isBefore(createExamRequestDto.getEnrollmentEndDate())) {
//            log.warn("Validation failed: enrollmentStartDate is not before enrollmentEndDate.");
//
//            model.addAttribute("error", "Enrollment start date must be before enrollment end date.");
//            return "instructor/exam-add";
//        }
//
//        if (!createExamRequestDto.getExamStartDate().isBefore(createExamRequestDto.getExamEndDate())) {
//            log.warn("Validation failed: examStartDate is not before examEndDate.");
//
//            model.addAttribute("error", "Exam start date must be before exam end date.");
//            return "instructor/exam-add";
//        }
//
//        if (!createExamRequestDto.getEnrollmentEndDate().isBefore(createExamRequestDto.getExamStartDate())) {
//            log.warn("Validation failed: enrollmentEndDate is not before examStartDate.");
//
//            model.addAttribute("error", "Enrollment must end before the exam starts.");
//            return "instructor/exam-add";
//        }

        try {
            examService.instructorCreateExam(createExamRequestDto, (Long) session.getAttribute(USER_ID));
            log.info("Exam created with description: {}", createExamRequestDto.getDescription());

            redirectAttributes.addFlashAttribute("message", "Exam created successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /instructor/manage/exams");
            return "redirect:/instructor/manage/exams";
        } catch (InsufficientQuestionsException ex) {
            log.error("Critical error: InSufficientQuestions", ex);

            redirectAttributes.addFlashAttribute("message", "Unable to complete the operation due to insufficient available questions");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/manage/exams");
            return "redirect:/instructor/manage/exams";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(
            HttpSession httpSession,
            Model model
    ) {
        Long baseUserId = (Long) httpSession.getAttribute(USER_ID);
        model.addAttribute("stats", instructorUserService.getInstructorUserStats(baseUserId, baseUserService.findByIdAndGetZoneId(baseUserId)));

        log.info("Rendering profile-view page");
        return "instructor/profile-view";
    }

    @GetMapping("/view/instructor/questions")
    public String getManageInstructorCreatedQuestionsPage(
            @Valid @ModelAttribute("filter") InstructorCreatedQuestionsFilterDto instructorCreatedQuestionsFilterDto,
            HttpSession httpSession,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Long instructorUserId = (Long) httpSession.getAttribute(USER_ID);

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            return "instructor/question-history-view";
        }

        try {
            QuestionPageViewDto questionViewPage = questionService.getFilteredInstructorCreatedQuestionsForInstructor(instructorCreatedQuestionsFilterDto, instructorUserId);

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            model.addAttribute("filter", instructorCreatedQuestionsFilterDto);

            model.addAttribute("questions", questionViewPage.getQuestions());
            model.addAttribute("pageNumber", questionViewPage.getPage());
            model.addAttribute("pageSize", questionViewPage.getPageSize());
            model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());

            log.info("Rendering question-history-view page");
            return "instructor/question-history-view";
        } catch (InstructorUserNotFoundException ex) {
            log.error("Instructor not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /instructor/home");
            return "redirect:/instructor/home";
        }
    }

    @GetMapping("/view/instructor/exams")
    public String getManageInstructorCreatedExamsPage(
            @Valid @ModelAttribute("filter") InstructorCreatedExamsFilterDto instructorCreatedExamsFilterDto,
            HttpSession httpSession,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Long instructorUserId = (Long) httpSession.getAttribute(USER_ID);

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            return "instructor/exam-history-view";
        }

        try {
            ExamPageViewDto examViewPageDto = examService.getFilteredInstructorCreatedExamsForInstructor(instructorCreatedExamsFilterDto, instructorUserId);

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            model.addAttribute("filter", instructorCreatedExamsFilterDto);

            model.addAttribute("exams", examViewPageDto.getExams());
            model.addAttribute("pageNumber", examViewPageDto.getPage());
            model.addAttribute("pageSize", examViewPageDto.getPageSize());
            model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());

            log.info("Rendering question-history-view page");
            return "instructor/exam-history-view";
        } catch (InstructorUserNotFoundException ex) {
            log.error("Instructor not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/home");
            return "redirect:/admin/home";
        }
    }

}
