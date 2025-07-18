package com.example.qbankapi.controller.admin;

import com.example.qbankapi.dto.model.*;
import com.example.qbankapi.dto.request.*;
import com.example.qbankapi.dto.view.*;
import com.example.qbankapi.exception.base.BaseUserNotFoundException;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.stream.Collectors;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SubjectService subjectService;
    private final QuestionService questionService;
    private final ExamService examService;
    private final BaseUserService baseUserService;
    private final ParticipantUserService participantUserService;
    private final InstructorUserService instructorUserService;

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

    @GetMapping("/view/instructors")
    public String getSubjectAssignedInstructorsPage(
            @RequestParam("subjectId") Long subjectId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute("subjectInstructors", subjectService.getSubjectAssignedInstructorsDtoById(subjectId, baseUserService.findByIdAndGetZoneId((Long) httpSession.getAttribute(USER_ID))));

            log.info("Rendering subject-instructor-view page");
            return "admin/subject-instructor-view";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", subjectId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/subjects";
        }
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
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            return "admin/subject-add";
        }

        try {
            subjectService.adminAddSubject(addSubjectRequest, (Long) httpSession.getAttribute(USER_ID));
            log.debug("Subject added with name [{}]", addSubjectRequest.getName());

            redirectAttributes.addFlashAttribute("message", "Subject added successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        } catch (SubjectAlreadyExistsException ex) {
            log.warn("Subject already exists with name [{}]", addSubjectRequest.getName(), ex);

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

    @GetMapping("/manage/subjects/{subjectId}/edit")
    public String getEditSubjectPage(
            @PathVariable("subjectId") Long subjectId,
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
            log.error("Subject not found with id [{}]", subjectId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        }
    }

    @PostMapping("/manage/subjects/edit")
    public String editSubject(
            @Valid @ModelAttribute("updateSubjectRequest") UpdateSubjectRequestDto updateSubjectRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            return "admin/subject-edit";
        }

        try {
            subjectService.adminUpdateSubject(updateSubjectRequest);
            log.debug("Subject created with name [{}]", updateSubjectRequest.getName());

            redirectAttributes.addFlashAttribute("message", "Subject updated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", updateSubjectRequest.getId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not updated.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/subjects");
            return "redirect:/admin/manage/subjects";
        }
    }

    @GetMapping("/manage/questions")
    public String getManageQuestionsPage(
            @Valid @ModelAttribute("filter") AllQuestionFilterDto allQuestionFilterDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            return "admin/question-manage";
        }

        QuestionPageViewDto questionViewPage = questionService.getFilteredQuestionsForAdmin(allQuestionFilterDto);

        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
        model.addAttribute("filter", allQuestionFilterDto);

        model.addAttribute("questions", questionViewPage.getQuestions());
        model.addAttribute("pageNumber", questionViewPage.getPage());
        model.addAttribute("pageSize", questionViewPage.getPageSize());
        model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());

        log.info("Rendering question-manage page");
        return "admin/question-manage";
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
            return "admin/question-analytics";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id [{}]", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @GetMapping("/manage/questions/add")
    public String getAddQuestionPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
        model.addAttribute("addQuestionRequest", new AddQuestionRequestDto());

        log.info("Rendering question-add page");
        return "admin/question-add";
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
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            return "admin/question-add";
        }

        try {
            questionService.adminAddQuestion(addQuestionRequest, (Long) httpSession.getAttribute(USER_ID));

            redirectAttributes.addFlashAttribute("message", "Question added successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", addQuestionRequest.getSubjectId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not created.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @GetMapping("/manage/questions/{questionId}/edit")
    public String getEditQuestionPage(
            @PathVariable("questionId") Long questionId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
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

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            model.addAttribute("updateQuestionRequest", updateQuestionRequest);

            log.info("Rendering question-edit page");
            return "admin/question-edit";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id [{}]", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @PostMapping("/manage/questions/edit")
    public String editQuestion(
            @Valid @ModelAttribute("updateQuestionRequest") UpdateQuestionRequestDto updateQuestionRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            return "admin/question-edit";
        }

        try {
            questionService.adminUpdateQuestion(updateQuestionRequest);
            log.debug("Question updated with id [{}]", updateQuestionRequest.getId());

            redirectAttributes.addFlashAttribute("message", "Question updated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id [{}]", updateQuestionRequest.getId(), ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", updateQuestionRequest.getSubjectId(), ex);

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

    @GetMapping("/manage/questions/{questionId}/activate")
    public String activateQuestion(
            @PathVariable("questionId") Long questionId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            questionService.activateQuestion(questionId);

            log.debug("Question activated with id [{}]", questionId);

            redirectAttributes.addFlashAttribute("message", "Question activated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id [{}]", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not activated.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @GetMapping("/manage/questions/{questionId}/deactivate")
    public String removeQuestion(
            @PathVariable("questionId") Long questionId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            questionService.deactivateQuestion(questionId);

            log.debug("Question deactivated with id [{}]", questionId);

            redirectAttributes.addFlashAttribute("message", "Question deactivated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (QuestionNotFoundException ex) {
            log.error("Question not found with id [{}]", questionId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        } catch (DataIntegrityViolationException ex) {
            log.error("Critical error: DataIntegrityViolation", ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Question not deleted.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/questions");
            return "redirect:/admin/manage/questions";
        }
    }

    @GetMapping("/manage/exams")
    public String getManageExamsPage(
            @Valid @ModelAttribute("filter") AllExamFilterDto allExamFilterDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            return "admin/exam-manage";
        }

        ExamPageViewDto examViewPageDto = examService.getFilteredExamsForAdmin(allExamFilterDto);

        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
        model.addAttribute("filter", allExamFilterDto);

        model.addAttribute("exams", examViewPageDto.getExams());
        model.addAttribute("pageNumber", examViewPageDto.getPage());
        model.addAttribute("pageSize", examViewPageDto.getPageSize());
        model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());

        log.info("Rendering exam-manage page");
        return "admin/exam-manage";
    }

    @GetMapping("/manage/exams/{examId}/analytics")
    public String getExamsAnalyticsPage(
            @PathVariable("examId") Long examId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ExamAnalyticsViewDto examAnalyticsViewDto = examService.getExamAnalytics(examId, baseUserService.findByIdAndGetZoneId((Long) httpSession.getAttribute(USER_ID)));

            model.addAttribute("examAnalytics", examAnalyticsViewDto);

            log.info("Rendering exam-analytics page");
            return "admin/exam-analytics";
        } catch (ExamNotFoundException ex) {
            log.error("Exam not found with id [{}]", examId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Exam not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/exams";
        }
    }

    @GetMapping("/manage/exams/create")
    public String getCreateExamPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
        model.addAttribute("createExamRequest", new CreateExamRequestDto());

        log.info("Rendering exam-add page");
        return "admin/exam-add";
    }

    @PostMapping("/manage/exams/save")
    public String createExam(
            @Valid @ModelAttribute("createExamRequest") CreateExamRequestDto createExamRequestDto,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            return "admin/exam-add";
        }

//        if (!createExamRequestDto.getEnrollmentStartDate().isBefore(createExamRequestDto.getEnrollmentEndDate())) {
//            log.warn("Validation failed: enrollmentStartDate is not before enrollmentEndDate");
//
//            model.addAttribute("error", "Enrollment start date must be before enrollment end date.");
//            return "admin/exam-add";
//        }
//
//        if (!createExamRequestDto.getExamStartDate().isBefore(createExamRequestDto.getExamEndDate())) {
//            log.warn("Validation failed: examStartDate is not before examEndDate");
//
//            model.addAttribute("error", "Exam start date must be before exam end date.");
//            return "admin/exam-add";
//        }
//
//        if (!createExamRequestDto.getEnrollmentEndDate().isBefore(createExamRequestDto.getExamStartDate())) {
//            log.warn("Validation failed: enrollmentEndDate is not before examStartDate");
//
//            model.addAttribute("error", "Enrollment must end before the exam starts.");
//            return "admin/exam-add";
//        }

        try {
            examService.adminCreateExam(createExamRequestDto, (Long) httpSession.getAttribute(USER_ID));
            log.info("Exam created with description [{}]", createExamRequestDto.getDescription());

            redirectAttributes.addFlashAttribute("message", "Exam created successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/exams";
        } catch (InsufficientQuestionsException ex) {
            log.error("Critical error: InSufficientQuestions", ex);

            redirectAttributes.addFlashAttribute("message", "Unable to complete the operation due to insufficient available questions");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/exams";
        }
    }

    @GetMapping("/manage/users")
    public String getManageUsersPage(
            @Valid @ModelAttribute("filter") BaseUserFilterDto userFilterDto,
            Model model,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/user-manage";
        }

        BaseUserPageViewDto baseUserViewPageDto = baseUserService.getFilteredUsersForAdmin(userFilterDto);
        model.addAttribute("filter", userFilterDto);

        model.addAttribute("baseUsers", baseUserViewPageDto.getBaseUsers());
        model.addAttribute("pageNumber", baseUserViewPageDto.getPage());
        model.addAttribute("pageSize", baseUserViewPageDto.getPageSize());
        model.addAttribute("hasNextPage", baseUserViewPageDto.getHasNextPage());

        log.info("Rendering user-manage page");
        return "admin/user-manage";
    }

    @GetMapping("/view/users/instructor/{instructorUserId}/profile")
    public String getInstructorUserProfileViewPage(
            @PathVariable("instructorUserId") Long instructorUserId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            InstructorUserProfileStatsViewDto instructorUserProfileStatsViewDto = instructorUserService.getInstructorUserStats(instructorUserId, baseUserService.findByIdAndGetZoneId((Long) httpSession.getAttribute(USER_ID)));

            model.addAttribute("availableSubjects", subjectService.getAvailableSubjectViewDtoList(
                    instructorUserProfileStatsViewDto
                            .getAssignedSubjects()
                            .stream()
                            .map(InstructorUserProfileStatsViewDto.AssignedSubjectViewDto::getId)
                            .collect(Collectors.toSet())
            ));
            model.addAttribute("instructorUserProfileStats", instructorUserProfileStatsViewDto);

            log.info("Rendering profile-view page");
            return "admin/profile-view";
        } catch (InstructorUserNotFoundException ex) {
            log.error("Instructor user not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "Instructor user not found");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/users";
        }
    }

    @GetMapping("/view/users/participant/{participantUserId}/profile")
    public String getProfileViewPage(
            @PathVariable("participantUserId") Long participantUserId,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute("participantUserProfileStats", participantUserService.getParticipantUserStats(participantUserId, baseUserService.findByIdAndGetZoneId((Long) httpSession.getAttribute(USER_ID))));

            log.info("Rendering profile-view page");
            return "admin/profile-view";
        } catch (ParticipantUserNotFoundException ex) {
            log.error("Participant user not found for id [{}]", participantUserId, ex);

            redirectAttributes.addFlashAttribute("message", "User not found");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/exams");
            return "redirect:/admin/manage/users";
        }
    }

    @GetMapping("/manage/users/instructor/{instructorUserId}/questions")
    public String getManageInstructorCreatedQuestionsPage(
            @PathVariable("instructorUserId") Long instructorUserId,
            @Valid @ModelAttribute("filter") InstructorCreatedQuestionsFilterDto instructorCreatedQuestionsFilterDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            model.addAttribute("instructorId", instructorUserId);
            return "admin/question-instructor-manage";
        }

        try {
            QuestionPageViewDto questionViewPage = questionService.getFilteredInstructorCreatedQuestionsForAdmin(instructorCreatedQuestionsFilterDto, instructorUserId);

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            model.addAttribute("filter", instructorCreatedQuestionsFilterDto);

            model.addAttribute("questions", questionViewPage.getQuestions());
            model.addAttribute("pageNumber", questionViewPage.getPage());
            model.addAttribute("pageSize", questionViewPage.getPageSize());
            model.addAttribute("hasNextPage", questionViewPage.getHasNextPage());

            model.addAttribute("instructorId", instructorUserId);

            log.info("Rendering question-manage page");
            return "admin/question-instructor-manage";
        } catch (InstructorUserNotFoundException ex) {
            log.error("Instructor not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/home");
            return "redirect:/admin/home";
        }
    }

    @GetMapping("/manage/users/instructor/{instructorUserId}/exams")
    public String getManageInstructorCreatedExamsPage(
            @PathVariable("instructorUserId") Long instructorUserId,
            @Valid @ModelAttribute("filter") InstructorCreatedExamsFilterDto instructorCreatedExamsFilterDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed: {}", bindingResult.getAllErrors());

            model.addAttribute("instructorId", instructorUserId);
            return "admin/exam-instructor-manage";
        }

        try {
            ExamPageViewDto examViewPageDto = examService.getFilteredInstructorCreatedExamsForAdmin(instructorCreatedExamsFilterDto, instructorUserId);

            model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
            model.addAttribute("filter", instructorCreatedExamsFilterDto);

            model.addAttribute("exams", examViewPageDto.getExams());
            model.addAttribute("pageNumber", examViewPageDto.getPage());
            model.addAttribute("pageSize", examViewPageDto.getPageSize());
            model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());

            model.addAttribute("instructorId", instructorUserId);

            log.info("Rendering exam-manage page");
            return "admin/exam-instructor-manage";
        } catch (InstructorUserNotFoundException ex) {
            log.error("Instructor not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/home");
            return "redirect:/admin/home";
        }
    }

    @PostMapping("/manage/users/{userId}/assign-subject")
    public String assignSubjectToInstructor(
            @PathVariable("userId") Long instructorUserId,
            @RequestParam("subjectId") Long subjectId,
            RedirectAttributes redirectAttributes) {
        try {
            instructorUserService.assignSubject(instructorUserId, subjectId);

            redirectAttributes.addFlashAttribute("message", "Subject assigned successfully.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            return "redirect:" + UriComponentsBuilder
                    .fromPath("/admin/view/users/instructor/{userId}/profile")
                    .buildAndExpand(instructorUserId)
                    .toUriString();
        } catch (InstructorUserNotFoundException ex) {
            log.error("InstructorUser not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", subjectId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        }
    }

    @PostMapping("/manage/users/{userId}/revoke-subject")
    public String revokeSubjectFromInstructor(
            @PathVariable("userId") Long instructorUserId,
            @RequestParam("subjectId") Long subjectId,
            RedirectAttributes redirectAttributes) {
        try {
            instructorUserService.revokeSubject(instructorUserId, subjectId);

            redirectAttributes.addFlashAttribute("message", "Subject revoked successfully.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            return "redirect:" + UriComponentsBuilder
                    .fromPath("/admin/view/users/instructor/{userId}/profile")
                    .buildAndExpand(instructorUserId)
                    .toUriString();
        } catch (InstructorUserNotFoundException ex) {
            log.error("InstructorUser not found with id [{}]", instructorUserId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. instructor not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        } catch (SubjectNotFoundException ex) {
            log.error("Subject not found with id [{}]", subjectId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. Subject not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        }
    }

    @GetMapping("/manage/users/{userId}/activate")
    public String activateUser(
            @PathVariable("userId") Long userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            baseUserService.activateUser(userId);
            log.debug("BaseUser activated with id [{}]", userId);

            redirectAttributes.addFlashAttribute("message", "User account activated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        } catch (BaseUserNotFoundException ex) {
            log.error("BaseUser not found with id [{}]", userId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. BaseUser not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        }
    }

    @GetMapping("/manage/users/{userId}/inactivate")
    public String inactivateUser(
            @PathVariable("userId") Long userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            baseUserService.inactivateUser(userId);
            log.debug("BaseUser inactivated with id [{}]", userId);

            redirectAttributes.addFlashAttribute("message", "User account inactivated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        } catch (BaseUserNotFoundException ex) {
            log.error("BaseUser not found with id [{}]", userId, ex);

            redirectAttributes.addFlashAttribute("message", "An unexpected error occurred. BaseUser not found.");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Redirecting to /admin/manage/users");
            return "redirect:/admin/manage/users";
        }
    }

    @ExceptionHandler(AdminUserNotFoundException.class)
    public String handleAdminUserNotFoundException(AdminUserNotFoundException ex) {
        log.warn("Admin user not found: {}", ex.getMessage());

        log.info("Redirecting to /admin/home");
        return "redirect:/admin/home";
    }

}
