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

    @GetMapping("/home")
    @ResponseBody
    public String hello(){
        log.info("Returning hello");
        return "hello";
    }

//    private final UserService userService;
//    private final SubjectService subjectService;
//    private final ExamService examService;
//    private final UserExamResultService userExamResultService;

//    @GetMapping("/home")
//    public String home(
//            Model model,
//            HttpSession session
//    ) {
//        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
//        User user = userService.findById(userId);
//
//        model.addAttribute("username", user.getUsername());
//        return "home";
//    }
//
//    @GetMapping("/subjects")
//    public String subjects(Model model) {
//        model.addAttribute("subjects", subjectService.getAllInDto());
//        return "subjects";
//    }
//
//    @GetMapping("/exams")
//    public String exams(@RequestParam(name = "subjectId", required = false) Optional<Long> optionalId, Model model) {
//        optionalId.ifPresentOrElse(
//                id -> {
//                    model.addAttribute("subject", subjectService.getInDtoById(id));
//                    model.addAttribute("exams", subjectService.getSubjectExamsInDtoById(id));
//                },
//                () -> model.addAttribute("exams", examService.getAllInDto())
//        );
//        return "exams";
//    }
//
//    @GetMapping("/history")
//    public String history(HttpSession session, Model model) {
//        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
//        model.addAttribute("history", userService.getEnrolledExamDtos(userId));
//        return "history";
//    }
//
//    @GetMapping("/profile")
//    public String profile(HttpSession session, Model model) {
//        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
//        model.addAttribute("stats", userService.getUserStats(userId));
//        return "profile";
//    }
//
//    @GetMapping("/result/exam/{id}")
//    public String examResult(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("result", userExamResultService.getDtoById(id));
//        return "examResult";
//    }

}
