package com.example.qbankapi.controller.instructor;

import com.example.qbankapi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorController {

    private final SubjectService subjectService;
//    private final QuestionService questionService;
//    private final ExamService examService;

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

//    @GetMapping("/manage/questions")
//    public String getManageQuestionsPage(
//            @Valid @ModelAttribute("filter") QuestionFilterDto questionFilterDto,
//            HttpSession httpSession,
//            BindingResult bindingResult,
//            Model model
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation Errors: {}", bindingResult.getAllErrors());
//            return "instructor/question-manage";
//        }
//
//        Long instructorId = (Long) httpSession.getAttribute(USER_ID);
//
//        QuestionPageViewDto questionViewPage = questionService.getFilteredQuestionsForInstructor(questionFilterDto, instructorId);
//
//        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoListForInstructor(instructorId));
//        model.addAttribute("filter", questionFilterDto);
//
//        model.addAttribute("questions", questionViewPage.getQuestions());
//        model.addAttribute("pageNumber", questionViewPage.getPage());
//        model.addAttribute("pageSize", questionViewPage.getPageSize());
//        model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());
//
//        log.info("Rendering question-manage page");
//        return "instructor/question-manage";
//    }

//    @GetMapping("/manage/instructor/questions")
//    public String getManageInstructorQuestionsPage(
//            @Valid @ModelAttribute("filter") QuestionFilterDto questionFilterDto,
//            BindingResult bindingResult,
//            HttpSession httpSession,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            model.addAttribute("instructor", true);
//            return "instructor/question-manage";
//        }
//
//        long instructorId = (Long) httpSession.getAttribute(USER_ID);
//        try {
//            QuestionPageViewDto questionViewPage = questionService.getFilteredInstructorCreatedQuestions(questionFilterDto, instructorId);
//
//            model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoList(instructorId));
//            model.addAttribute("filter", questionFilterDto);
//
//            model.addAttribute("questions", questionViewPage.getQuestions());
//            model.addAttribute("pageNumber", questionViewPage.getPage());
//            model.addAttribute("pageSize", questionViewPage.getPageSize());
//            model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());
//            model.addAttribute("instructor", true);
//
//            log.info("Rendering question-manage page");
//            return "instructor/question-manage";
//        } catch (InstructorUserNotFoundException ex) {
//            log.error("Instructor not found with id: {}", instructorId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Instructor not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/dashboard";
//        }
//    }
//
//    @GetMapping("/manage/questions/{questionId}/analytics")
//    public String getQuestionAnalyticsPage(
//            @PathVariable("questionId") Long questionId,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            QuestionAnalyticsViewDto questionAnalyticsViewDto = questionService.getQuestionAnalytics(questionId);
//
//            model.addAttribute("questionAnalytics", questionAnalyticsViewDto);
//
//            log.info("Rendering question-analytics page");
//            return "instructor/question-analytics";
//        } catch (QuestionNotFoundException ex) {
//            log.error("Question not found with id: {}", questionId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/manage/questions";
//        }
//    }
//
//    @GetMapping("/manage/questions/add")
//    public String getAddQuestionPage(
//            HttpSession httpSession,
//            Model model
//    ) {
//        model.addAttribute("subjects", subjectService.getAssignedSubjectViewDtoList((Long) httpSession.getAttribute(USER_ID)));
//        model.addAttribute("addQuestionRequest", new AddQuestionRequestDto());
//
//        log.info("Rendering question-add page");
//        return "instructor/question-add";
//    }
//
//    @PostMapping("/manage/questions/save")
//    public String addQuestion(
//            @Valid @ModelAttribute("addQuestionRequest") AddQuestionRequestDto addQuestionRequest,
//            BindingResult bindingResult,
//            HttpSession httpSession,
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
//            Long instructorId = (Long) httpSession.getAttribute(USER_ID);
//
//            subjectService.isSubjectAssigned(instructorId, addQuestionRequest.getSubjectId());
//
//            questionService.addQuestion(addQuestionRequest, instructorId);
//
//            redirectAttributes.addFlashAttribute("message", "Question added successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/manage/questions";
//        } catch (SubjectNotFoundException | AccessDeniedException ex) {
//            log.error("Subject not found with id: {}", addQuestionRequest.getSubjectId(), ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/manage/questions";
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Critical error: DataIntegrityViolation", ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not created.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/manage/questions";
//        }
//    }
//
//    @GetMapping("/manage/questions/{questionId}/edit")
//    public String getEditQuestionPage(
//            @PathVariable("questionId") Long questionId,
//            HttpSession httpSession,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            questionService.isQuestionCreatedByInstructor(questionId, (Long) httpSession.getAttribute(USER_ID));
//
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
//            return "instructor/question-edit";
//        } catch (QuestionNotFoundException | AccessDeniedException ex) {
//            log.error("Question not found with id: {}", questionId, ex);
//
//            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/questions");
//            return "redirect:/instructor/manage/questions";
//        }
//    }
//
//    @PostMapping("/manage/questions/edit")
//    public String editQuestion(
//            @Valid @ModelAttribute("updateQuestionRequest") UpdateQuestionRequestDto updateQuestionRequest,
//            HttpSession httpSession,
//            Model model,
//            BindingResult bindingResult,
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
//            questionService.isQuestionCreatedByInstructor(updateQuestionRequest.getId(), (Long) httpSession.getAttribute(USER_ID));
//
//            questionService.updateQuestion(updateQuestionRequest);
//            log.debug("Question updated with id: {}", updateQuestionRequest.getId());
//
//            redirectAttributes.addFlashAttribute("message", "Question updated successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /admin/manage/questions");
//            return "redirect:/admin/manage/questions";
//        } catch (QuestionNotFoundException | AccessDeniedException ex) {
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

//    @GetMapping("/manage/exams")
//    public String getManageExamsPage(
//            @Valid @ModelAttribute("filter") ExamFilterDto examFilterDto,
//            Model model,
//            BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());
//
//            return "instructor/exam-manage";
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
//        return "instructor/exam-manage";
//    }
//
//    @GetMapping("/manage/exams/create")
//    public String getCreateExamPage(Model model) {
//        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
//        model.addAttribute("createExamRequest", new CreateExamRequestDto());
//
//        log.info("Rendering exam-add page");
//        return "instructor/exam-add";
//    }

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
//            return "instructor/exam-add";
//        }
//
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
//
//        try {
//            examService.createExam(createExamRequestDto, (Long) session.getAttribute(USER_ID));
//            log.info("Exam created with description: {}", createExamRequestDto.getDescription());
//
//            redirectAttributes.addFlashAttribute("message", "Exam created successfully");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//
//            log.info("Redirecting to /instructor/manage/exams");
//            return "redirect:/instructor/manage/exams";
//        } catch (InsufficientQuestionsException ex) {
//            log.error("Critical error: InSufficientQuestions", ex);
//
//            redirectAttributes.addFlashAttribute("message", "Unable to complete the operation due to insufficient available questions");
//            redirectAttributes.addFlashAttribute("messageType", "error");
//
//            log.info("Redirecting to /instructor/manage/exams");
//            return "redirect:/instructor/manage/exams";
//        }
//    }

}
