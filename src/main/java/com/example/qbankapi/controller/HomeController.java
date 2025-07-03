package com.example.qbankapi.controller;

import com.example.qbankapi.dto.request.LoginBaseUserRequestDto;
import com.example.qbankapi.dto.request.RegisterBaseUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.exception.base.impl.AccountNotActiveException;
import com.example.qbankapi.exception.base.impl.AdminUserNotFoundException;
import com.example.qbankapi.exception.base.impl.InstructorUserNotFoundException;
import com.example.qbankapi.exception.base.impl.ParticipantUserNotFoundException;
import com.example.qbankapi.service.AuthenticationService;
import com.example.qbankapi.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final BaseUserService baseUserService;

    @GetMapping("/")
    public String index(HttpSession session) {
        BaseUser.Role role = (BaseUser.Role) session.getAttribute(USER_ROLE);
        log.info("Session role: {}", role);

        String redirectUrl = switch (role) {
            case ADMIN -> "/admin/home";
            case INSTRUCTOR -> "/instructor/home";
            case PARTICIPANT -> "/participant/home";
            default -> "/login";
        };

        log.info("Redirecting to: {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/register")
    public String getRegisterPage(@RequestParam("role") Optional<RegisterBaseUserRequestDto.Role> optionalRole, Model model, HttpSession session) {
        Object isBaseUserVerified = session.getAttribute(IS_USER_VERIFIED);
        if (isBaseUserVerified != null) {
            if (Boolean.TRUE.equals(isBaseUserVerified)) {
                log.debug("BaseUser already authenticated");

                log.info("Redirecting to: /");
                return "redirect:/";
            }
        }

        if (optionalRole.isPresent()) {
            RegisterBaseUserRequestDto.Role role = optionalRole.get();
            switch (role) {
                case INSTRUCTOR -> model.addAttribute("role", RegisterBaseUserRequestDto.Role.INSTRUCTOR.name());
                case PARTICIPANT -> model.addAttribute("role", RegisterBaseUserRequestDto.Role.PARTICIPANT.name());
                default -> log.warn("Invalid parameter passed role: " + role);
            }
        }
        model.addAttribute("registerBaseUserRequest", new RegisterBaseUserRequestDto());

        log.info("Rendering register page");
        return "register";
    }

    @PostMapping("/register")
    public String registerBaseUser(
            @Valid @ModelAttribute("registerBaseUserRequest") RegisterBaseUserRequestDto registerBaseUserRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        log.info("Register attempt with username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Login validation failed for username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());

            model.addAttribute("role", registerBaseUserRequest.getRole());
            return "register";
        }

        if (baseUserService.validateUsernameIfExists(registerBaseUserRequest.getUsername())) {
            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("username", "Username.Invalid", "Username already exists.");
            return "register";
        } else if (baseUserService.validateEmailIfExists(registerBaseUserRequest.getEmail())) {
            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("email", "Email.Invalid", "Email already exists.");
            return "register";
        } else if (!registerBaseUserRequest.getPassword().equals(registerBaseUserRequest.getConfirmPassword())) {
            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("confirmPassword", "ConfirmPassword.Invalid", "Password and confirm password must be same.");
            return "register";
        }

        try {
            Optional<BaseUser> optionalBaseUser = baseUserService.registerBaseUser(registerBaseUserRequest);
            if (optionalBaseUser.isPresent()) {
                BaseUser baseUser = optionalBaseUser.get();

                System.out.println("___");
                System.out.println(baseUser);
                log.info("Register successful for BaseUser ID: {}, Role: {}", baseUser.getId(), baseUser.getRole());

                session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
                session.setAttribute(USER_ID, baseUser.getId());
                session.setAttribute(USER_ROLE, baseUser.getRole());
                log.info("BaseUser attributes set in session IS_USER_VERIFIED, USER_ID, USER_ROLE");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Register failed for BaseUser username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());
                model.addAttribute("message", "Unexpected role type provided. Please contact support.");
                return "error";
            }
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("message", "User already exists with the provided email or username.");
            return "error";
        }
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        Object isBaseUserVerified = session.getAttribute(IS_USER_VERIFIED);
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
            if (!loginBaseUserRequest.getBaseUserIdentifier().matches("^[A-Za-z0-9_.]{3,20}$")) {
                bindingResult.rejectValue("baseUserIdentifier", "Username.Invalid", "Username must be alphanumeric and 3-20 characters long.");
                return "login";
            }
        }

        try {
            Optional<BaseUser> optionalBaseUser = authenticationService.authenticateBaseUser(loginBaseUserRequest);
            if (optionalBaseUser.isPresent()) {
                BaseUser baseUser = optionalBaseUser.get();
                log.info("Authentication successful for BaseUser ID: {}, Role: {}", baseUser.getId(), baseUser.getRole());

                session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
                session.setAttribute(USER_ID, baseUser.getId());
                session.setAttribute(USER_ROLE, baseUser.getRole());
                log.info("BaseUser attributes set in session IS_USER_VERIFIED, USER_ID, USER_ROLE");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Authentication failed for BaseUser identifier: {}", loginBaseUserRequest.getBaseUserIdentifier());
                model.addAttribute("error", "Invalid username/email or password");
                return "login";
            }
        } catch (AccountNotActiveException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        } catch (AdminUserNotFoundException | InstructorUserNotFoundException | ParticipantUserNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/logout")
    public String logoutBaseUser(HttpSession session) {
        log.info("Logout attempt");

        log.debug("Invalidating BaseUser session");
        session.invalidate();

        log.info("Redirecting to: /login");
        return "redirect:/login";
    }

}
