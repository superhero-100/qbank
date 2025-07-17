package com.example.qbankapi.controller.participant;

import com.example.qbankapi.entity.ParticipantUser;
import com.example.qbankapi.service.BaseUserService;
import com.example.qbankapi.service.ExamService;
import com.example.qbankapi.service.ParticipantUserService;
import com.example.qbankapi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/home")
    public String getDashboardPage(HttpServletRequest request, Model model, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute(USER_ID);
        ParticipantUser user = participantUserService.findById(userId);

        model.addAttribute("username", user.getUsername());

        log.info("Rendering dashboard page");
        return "participant/dashboard";
    }

    @GetMapping("/calendar")
    public String getParticipantCalenderPage(HttpServletRequest request, Model model, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute(USER_ID);
        ParticipantUser user = participantUserService.findById(userId);

        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("upcomingExams", examService.getUpcomingExams(userId));

        log.info("Rendering exams-upcoming page");
        return "participant/exams-upcoming";
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

//    @GetMapping("/history")
//    public String getHistoryPage(HttpSession session, Model model) {
//        Long userId = (Long) session.getAttribute(USER_ID);
//        model.addAttribute("history", participantUserService.getAllEnrolledExamDtoList(userId));
//        return "participant/history-view";
//    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session, Model model) {
        Long participantUserId = (Long) session.getAttribute(USER_ID);
        model.addAttribute("stats", participantUserService.getParticipantUserStats(participantUserId, baseUserService.findByIdAndGetZoneId(participantUserId)));

        log.info("Rendering profile-view page");
        return "participant/profile-view";
    }

//    @GetMapping("/result/exam/{id}")
//    public String getExamResultPage(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("result", participantUserExamResultService.getParticipantUserExamResultInDtoById(id));
//
//        log.info("Rendering exam-result page");
//        return "participant/exam-result";
//    }

}
