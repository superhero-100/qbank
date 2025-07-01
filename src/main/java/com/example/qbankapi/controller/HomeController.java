package com.example.qbankapi.controller;

import com.example.qbankapi.dto.request.LoginUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.exception.AccountNotActiveException;
import com.example.qbankapi.exception.AdminNotFoundException;
import com.example.qbankapi.exception.UserNotFoundException;
import com.example.qbankapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public String index(HttpSession session) {
        BaseUser.Role role = (BaseUser.Role) session.getAttribute(USER_ROLE);
        log.info("Session role: {}", role);

        String redirectUrl = switch (role) {
            case ADMIN -> "/admin/home";
            case USER -> "/user/home";
            default -> "/login";
        };

        log.info("Redirecting to: {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        Object isUserAerified = session.getAttribute(IS_USER_VERIFIED);
        if (isUserAerified != null) {
            if (Boolean.TRUE.equals(isUserAerified)) {
                log.debug("User already authenticated");
                log.info("Redirecting to: /");
                return "redirect:/";
            }
        }

        model.addAttribute("loginUserRequest", new LoginUserRequestDto());

        log.info("Rendering login page");
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("loginUserRequest") LoginUserRequestDto loginUserRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        log.info("Login attempt for username: {}", loginUserRequest.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Login validation failed for username: {}", loginUserRequest.getUsername());
            return "login";
        }

        try {
            Optional<BaseUser> optionalBaseUser = authenticationService.authenticate(loginUserRequest);
            if (optionalBaseUser.isPresent()) {
                BaseUser baseUser = optionalBaseUser.get();
                log.info("Authentication successful for user ID: {}, Role: {}", baseUser.getId(), baseUser.getRole());

                session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
                session.setAttribute(USER_ID, baseUser.getId());
                session.setAttribute(USER_ROLE, baseUser.getRole());
                log.info("User attributes set in session IS_USER_VERIFIED, USER_ID, USER_ROLE");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Authentication failed for username: {}", loginUserRequest.getUsername());
                model.addAttribute("error", "Invalid Username Or Password");
                return "login";
            }
        } catch (AccountNotActiveException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        } catch (UserNotFoundException | AdminNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        log.info("Logout attempt");

        log.debug("Removing attributes from user session");
        session.removeAttribute(IS_USER_VERIFIED);
        session.removeAttribute(USER_ID);
        session.removeAttribute(USER_ROLE);

        log.info("Redirecting to: /login");
        return "redirect:/login";
    }

}
