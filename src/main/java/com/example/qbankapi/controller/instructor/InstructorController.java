package com.example.qbankapi.controller.instructor;

import com.example.qbankapi.dto.model.ExamFilterDto;
import com.example.qbankapi.dto.request.CreateExamRequestDto;
import com.example.qbankapi.dto.view.ExamPageViewDto;
import com.example.qbankapi.exception.base.impl.InsufficientQuestionsException;
import com.example.qbankapi.service.ExamService;
import com.example.qbankapi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorController {

    private final ExamService examService;
    private final SubjectService subjectService;

    @GetMapping("/home")
    public String getDashboardPage() {
        log.info("Rendering instructor dashboard page");
        return "instructor/dashboard";
    }

    @GetMapping("/manage/exams")
    public String getManageExamsPage(
            @Valid @ModelAttribute("filter") ExamFilterDto examFilterDto,
            Model model,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            return "admin/exam-manage";
        }

        ExamPageViewDto examViewPageDto = examService.getFilteredExams(examFilterDto);

        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());
        model.addAttribute("filter", examFilterDto);

        model.addAttribute("exams", examViewPageDto.getExams());
        model.addAttribute("pageNumber", examViewPageDto.getPage());
        model.addAttribute("pageSize", examViewPageDto.getPageSize());
        model.addAttribute("hasNextPage", examViewPageDto.getHasNextPage());

        log.info("Rendering exam-manage page");
        return "admin/exam-manage";
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
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed. Errors: {}", bindingResult.getAllErrors());

            return "admin/exam-add";
        }

        if (!createExamRequestDto.getEnrollmentStartDate().isBefore(createExamRequestDto.getEnrollmentEndDate())) {
            log.warn("Validation failed: enrollmentStartDate is not before enrollmentEndDate.");

            model.addAttribute("error", "Enrollment start date must be before enrollment end date.");
            return "admin/exam-add";
        }

        if (!createExamRequestDto.getExamStartDate().isBefore(createExamRequestDto.getExamEndDate())) {
            log.warn("Validation failed: examStartDate is not before examEndDate.");

            model.addAttribute("error", "Exam start date must be before exam end date.");
            return "admin/exam-add";
        }

        if (!createExamRequestDto.getEnrollmentEndDate().isBefore(createExamRequestDto.getExamStartDate())) {
            log.warn("Validation failed: enrollmentEndDate is not before examStartDate.");

            model.addAttribute("error", "Enrollment must end before the exam starts.");
            return "admin/exam-add";
        }

        try {
            examService.createExam(createExamRequestDto, (Long) session.getAttribute(USER_ID));
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

}
