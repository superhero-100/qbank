package com.example.qbankapi.controller;

import com.example.qbankapi.dto.LoginUserRequestDto;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.interceptor.SessionValidationInterceptor;
import com.example.qbankapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ExamService examService;
    private final UserExamResultService userExamResultService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginUserRequest", new LoginUserRequestDto());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("loginUserRequest") LoginUserRequestDto loginUserRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        Optional<User> optionalUser = authenticationService.authenticate(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute(SessionValidationInterceptor.USER_ID, user.getId());
            return "redirect:/home";
        }
        model.addAttribute("error", "Invalid Username Or Password");
        return "login";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.removeAttribute(SessionValidationInterceptor.USER_ID);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(
            Model model,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
        User user = userService.findById(userId);

        model.addAttribute("username", user.getUsername());
        return "home";
    }

    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllInDto());
        return "subjects";
    }

    @GetMapping("/exams")
    public String exams(@RequestParam(name = "subjectId", required = false) Optional<Long> optionalId, Model model) {
        optionalId.ifPresentOrElse(
                id -> {
                    model.addAttribute("subject", subjectService.getInDtoById(id));
                    model.addAttribute("exams", subjectService.getSubjectExamsInDtoById(id));
                },
                () -> model.addAttribute("exams", examService.getAllInDto())
        );
        return "exams";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
        model.addAttribute("history", userService.getEnrolledExamDtos(userId));
        return "history";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
        model.addAttribute("stats", userService.getUserStats(userId));
        return "profile";
    }

    @GetMapping("/result/exam/{id}")
    public String examResult(@PathVariable("id") Long id, Model model) {
        model.addAttribute("result", userExamResultService.getDtoById(id));
        return "examResult";
    }

}
