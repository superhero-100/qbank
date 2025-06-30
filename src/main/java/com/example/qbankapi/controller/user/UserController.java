package com.example.qbankapi.controller.user;

import com.example.qbankapi.entity.User;
import com.example.qbankapi.service.ExamService;
import com.example.qbankapi.service.SubjectService;
import com.example.qbankapi.service.UserExamResultService;
import com.example.qbankapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubjectService subjectService;
    private final ExamService examService;
    private final UserExamResultService userExamResultService;

    @GetMapping("/home")
    public String home(
            Model model,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute(USER_ID);
        User user = userService.findById(userId);

        model.addAttribute("username", user.getUsername());
        return "/user/dashboard";
    }

    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", subjectService.getSubjectDtoList());
        return "/user/subjects-view";
    }

    @GetMapping("/exams")
    public String exams(@RequestParam(name = "subjectId", required = false) Optional<Long> optionalId, Model model) {
        optionalId.ifPresentOrElse(
                id -> {
                    model.addAttribute("subject", subjectService.getSubjectDtoById(id));
                    model.addAttribute("exams", subjectService.getSubjectExamsInDtoById(id));
                },
                () -> model.addAttribute("exams", examService.getAllExamsInDto())
        );
        return "/user/exams-view";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(USER_ID);
        model.addAttribute("history", userService.getAllEnrolledExamDtos(userId));
        return "/user/history-view";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(USER_ID);
        model.addAttribute("stats", userService.getUserStats(userId));
        return "/user/profile-view";
    }

    @GetMapping("/result/exam/{id}")
    public String examResult(@PathVariable("id") Long id, Model model) {
        model.addAttribute("result", userExamResultService.getUserExamResultInDtoById(id));
        return "/user/exam-result";
    }

}
