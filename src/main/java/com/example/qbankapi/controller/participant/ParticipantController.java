package com.example.qbankapi.controller.participant;

import com.example.qbankapi.dao.ParticipantUserExamResultDao;
import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.exception.base.impl.AccessDeniedException;
import com.example.qbankapi.exception.base.impl.ParticipantUserExamEnrollmentNotFoundException;
import com.example.qbankapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantUserService participantUserService;
    private final SubjectService subjectService;
    private final ExamService examService;
    private final BaseUserService baseUserService;
    private final ParticipantUserExamResultService participantUserExamResultService;

    @GetMapping("/home")
    public String getDashboardPage(Model model, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute(USER_ID);
        ParticipantUser user = participantUserService.findById(userId);

        model.addAttribute("username", user.getUsername());

        log.info("Rendering dashboard page");
        return "participant/dashboard";
    }

    @GetMapping("/exams/today")
    public String getExamsTodayPage(Model model, HttpSession httpSession){
        Long userId = (Long) httpSession.getAttribute(USER_ID);

        model.addAttribute("todayExams", examService.getTodayExams(userId));

        log.info("Rendering exams-today page");
        return "participant/exams-today";
    }

    @GetMapping("/calendar")
    public String getParticipantCalenderPage(HttpServletRequest request, Model model, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute(USER_ID);

        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("calenderEvents", examService.getUserExamCalenderEvents(userId));

        log.info("Rendering exams-calender page");
        return "participant/exams-calender";
    }

    @GetMapping("/subjects")
    public String getSubjectsPage(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectViewDtoList());

        log.info("Rendering subjects-view page");
        return "participant/subjects-view";
    }

    @GetMapping("/exams")
    public String getExamsPage(@RequestParam(name = "subjectId", required = false) Long optionalId, HttpSession session, Model model) {
        if (optionalId != null) {
            model.addAttribute("subject", subjectService.getSubjectDtoById(optionalId));
            model.addAttribute("exams", examService.getExamsInDtoBySubjectId(optionalId, (Long) session.getAttribute(USER_ID)));
        } else {
            model.addAttribute("exams", examService.getAllExamsInDto((Long) session.getAttribute(USER_ID)));
        }

        log.info("Rendering exams-view page");
        return "participant/exams-view";
    }

    @GetMapping("/history")
    public String getHistoryPage(HttpSession session, Model model) {
        model.addAttribute("history", participantUserService.getAllEnrolledExamDtoList((Long) session.getAttribute(USER_ID)));

        log.info("Rendering history-view page");
        return "participant/history-view";
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session, Model model) {
        Long participantUserId = (Long) session.getAttribute(USER_ID);
        model.addAttribute("stats", participantUserService.getParticipantUserStats(participantUserId, baseUserService.findByIdAndGetZoneId(participantUserId)));

        log.info("Rendering profile-view page");
        return "participant/profile-view";
    }

    @GetMapping("/result/exam/{enrollmentId}")
    public String getExamResultPage(@PathVariable("enrollmentId") Long enrollmentId, HttpSession httpSession, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) httpSession.getAttribute(USER_ID);
        try {
            model.addAttribute("result", participantUserExamResultService.getParticipantUserExamResultInDtoByExamEnrollmentId(enrollmentId, userId));

            log.info("Rendering exam-result page");
            return "participant/exam-result";
        } catch (ParticipantUserExamEnrollmentNotFoundException ex) {
            log.error("Exam enrollment not found for ID {}: {}", enrollmentId, ex.getMessage());

            redirectAttributes.addFlashAttribute("message", "Exam result not found");
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Rendering /participant/history page");
            return "redirect:/participant/history";
        } catch (AccessDeniedException ex) {
            log.error("Access denied for userId={} on enrollmentId={}: {}", userId, enrollmentId, ex.getMessage());

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");

            log.info("Rendering /participant/history page");
            return "redirect:/participant/history";
        }
    }

}
