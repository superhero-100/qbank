package com.example.qbankapi.controller;

import com.example.qbankapi.dto.request.LoginBaseUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.exception.AccountNotActiveException;
import com.example.qbankapi.exception.AdminNotFoundException;
import com.example.qbankapi.exception.TeacherNotFoundException;
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
        BaseUser.Role role = (BaseUser.Role) session.getAttribute(BASE_USER_ROLE);
        log.info("Session role: {}", role);

        String redirectUrl = switch (role) {
            case ADMIN -> "/admin/home";
            case TEACHER -> "/teacher/home";
            case USER -> "/user/home";
            default -> "/login";
        };

        log.info("Redirecting to: {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        Object isBaseUserVerified = session.getAttribute(IS_BASE_USER_VERIFIED);
        if (isBaseUserVerified != null) {
            if (Boolean.TRUE.equals(isBaseUserVerified)) {
                log.debug("BaseUser already authenticated");

                log.info("Redirecting to: /");
                return "redirect:/";
            }
        }

        model.addAttribute("loginBaseUserRequest", new LoginBaseUserRequestDto());

        log.info("Rendering login page");
        return "login";
    }

    @PostMapping("/login")
    public String loginBaseUser(
            @Valid @ModelAttribute("loginBaseUserRequest") LoginBaseUserRequestDto loginBaseUserRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        log.info("Login attempt with BaseUser identifier: {}", loginBaseUserRequest.getBaseUserIdentifier());

        if (bindingResult.hasErrors()) {
            log.warn("Login validation failed for BaseUser identifier: {}", loginBaseUserRequest.getBaseUserIdentifier());
            return "login";
        }

        if (loginBaseUserRequest.getBaseUserIdentifier().contains("@")) {
            if (!loginBaseUserRequest.getBaseUserIdentifier().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                bindingResult.rejectValue("baseUserIdentifier", "Email.Invalid", "Invalid email format.");
                return "login";
            }
        } else {
            if (!loginBaseUserRequest.getBaseUserIdentifier().matches("^[A-Za-z0-9_]{3,20}$")) {
                bindingResult.rejectValue("baseUserIdentifier", "Username.Invalid", "Username must be alphanumeric and 3-20 characters long.");
                return "login";
            }
        }

        try {
            Optional<BaseUser> optionalBaseUser = authenticationService.authenticateBaseUser(loginBaseUserRequest);
            if (optionalBaseUser.isPresent()) {
                BaseUser baseUser = optionalBaseUser.get();
                log.info("Authentication successful for BaseUser ID: {}, Role: {}", baseUser.getId(), baseUser.getRole());

                session.setAttribute(IS_BASE_USER_VERIFIED, Boolean.TRUE);
                session.setAttribute(BASE_USER_ID, baseUser.getId());
                session.setAttribute(BASE_USER_ROLE, baseUser.getRole());
                log.info("User attributes set in session IS_BASE_USER_VERIFIED, BASE_USER_ID, BASE_USER_ROLE");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Authentication failed for BaseUser identifier: {}", loginBaseUserRequest.getBaseUserIdentifier());
                model.addAttribute("error", "Invalid Username|Email Or Password");
                return "login";
            }
        } catch (AccountNotActiveException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        } catch (AdminNotFoundException | UserNotFoundException | TeacherNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/logout")
    public String logoutBaseUser(HttpSession session) {
        log.info("Logout attempt");

//        log.debug("Removing attributes from user session");
//        session.removeAttribute(IS_BASE_USER_VERIFIED);
//        session.removeAttribute(BASE_USER_ID);
//        session.removeAttribute(BASE_USER_ROLE);

        log.debug("invalidating user session");
        session.invalidate();

        log.info("Redirecting to: /login");
        return "redirect:/login";
    }

}
