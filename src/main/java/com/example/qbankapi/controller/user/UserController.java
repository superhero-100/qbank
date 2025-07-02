package com.example.qbankapi.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

//    private final UserService userService;
//    private final SubjectService subjectService;
//    private final ExamService examService;
//    private final UserExamResultService userExamResultService;

//    @GetMapping("/home")
//    public String getDashboardPage(
//            Model model,
//            HttpSession session
//    ) {
//        Long userId = (Long) session.getAttribute(USER_ID);
//        User user = userService.findById(userId);
//
//        model.addAttribute("username", user.getUsername());
//        return "/user/dashboard";
//    }
//
//    @GetMapping("/subjects")
//    public String getSubjectsPage(Model model) {
//        model.addAttribute("subjects", subjectService.getSubjectDtoList());
//        return "/user/subjects-view";
//    }
//
//    @GetMapping("/exams")
//    public String getExamsPage(@RequestParam(name = "subjectId", required = false) Optional<Long> optionalId, HttpSession session, Model model) {
//        optionalId.ifPresentOrElse(
//                id -> {
//                    model.addAttribute("subject", subjectService.getSubjectDtoById(id));
//                    model.addAttribute("exams", examService.getExamsInDtoBySubjectId(id, (Long) session.getAttribute(USER_ID)));
//                },
//                () -> model.addAttribute("exams", examService.getAllExamsInDto((Long) session.getAttribute(USER_ID)))
//        );
//        return "/user/exams-view";
//    }
//
//    @GetMapping("/history")
//    public String getHistoryPage(HttpSession session, Model model) {
//        Long userId = (Long) session.getAttribute(USER_ID);
//        model.addAttribute("history", userService.getAllEnrolledExamDtos(userId));
//        return "/user/history-view";
//    }
//
//    @GetMapping("/profile")
//    public String getProfilePage(HttpSession session, Model model) {
//        Long userId = (Long) session.getAttribute(USER_ID);
//        model.addAttribute("stats", userService.getUserStats(userId));
//        return "/user/profile-view";
//    }
//
//    @GetMapping("/result/exam/{id}")
//    public String getExamResultPage(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("result", userExamResultService.getUserExamResultInDtoById(id));
//        return "/user/exam-result";
//    }

}
