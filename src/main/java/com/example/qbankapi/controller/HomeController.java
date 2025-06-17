package com.example.qbankapi.controller;

import com.example.qbankapi.dto.LoginUserRequestDto;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.interceptor.SessionValidationInterceptor;
import com.example.qbankapi.service.AuthenticationService;
import com.example.qbankapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/")
    public String index(HttpServletResponse response) throws IOException {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(Model model) throws IOException {
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
    ) throws IOException {
        Long userId = (Long) session.getAttribute(SessionValidationInterceptor.USER_ID);
        User user = userService.findById(userId);
        model.addAttribute("username", user.getUsername());
        return "home";
    }

}
