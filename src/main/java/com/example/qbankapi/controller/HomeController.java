package com.example.qbankapi.controller;

import com.example.qbankapi.dto.LoginUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.entity.User;
import com.example.qbankapi.entity.constant.Role;
import com.example.qbankapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Optional;

import static com.example.qbankapi.interceptor.constant.Variable.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public String index(HttpSession session) {
        Role role = (Role) session.getAttribute(USER_ROLE);
        String redirectUrl = switch (role) {
            case ADMIN -> "/admin/home";
            case USER -> "/user/home";
            default -> "/login";
        };
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/editor")
    public String editor() {
        return "editor";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute(IS_USER_VERIFIED) != null) {
            if (Boolean.TRUE.equals(session.getAttribute(IS_USER_VERIFIED))) {
                return "redirect:/";
            }
        }
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
        Optional<BaseUser> optionalBaseUser = authenticationService.authenticate(loginUserRequest);
        if (optionalBaseUser.isPresent()) {
            BaseUser baseUser = optionalBaseUser.get();
            session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
            session.setAttribute(USER_ID, baseUser.getId());
            session.setAttribute(USER_ROLE, baseUser.getRole());
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid Username Or Password");
        return "login";
    }

//    register routes
//    @GetMapping("/register")
//    public String login(Model model, HttpSession session) {
//        if (session.getAttribute(IS_USER_VERIFIED) != null) {
//            if (Boolean.TRUE.equals(session.getAttribute(IS_USER_VERIFIED))) {
//                return "redirect:/";
//            }
//        }
//        model.addAttribute("loginUserRequest", new LoginUserRequestDto());
//        return "login";
//    }
//
//    @PostMapping("/register")
//    public String loginUser(
//            @Valid @ModelAttribute("loginUserRequest") LoginUserRequestDto loginUserRequest,
//            BindingResult bindingResult,
//            Model model,
//            HttpSession session
//    ) {
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }
//        Optional<BaseUser> optionalBaseUser = authenticationService.authenticate(loginUserRequest);
//        if (optionalBaseUser.isPresent()) {
//            BaseUser baseUser = optionalBaseUser.get();
//            session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
//            session.setAttribute(USER_ID, baseUser.getId());
//            session.setAttribute(USER_ROLE, baseUser.getRole());
//            return "redirect:/";
//        }
//        model.addAttribute("error", "Invalid Username Or Password");
//        return "login";
//    }

}
