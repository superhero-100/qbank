package com.example.qbankapi.controller;

import com.example.qbankapi.dto.request.LoginBaseUserRequestDto;
import com.example.qbankapi.dto.request.RegisterBaseUserRequestDto;
import com.example.qbankapi.entity.BaseUser;
import com.example.qbankapi.exception.base.impl.*;
import com.example.qbankapi.service.AuthenticationService;
import com.example.qbankapi.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String index(HttpSession session, RedirectAttributes redirectAttributes, @ModelAttribute("message") String message, @ModelAttribute("messageType") String messageType) {
        BaseUser.Role role = (BaseUser.Role) session.getAttribute(USER_ROLE);
        log.info("Session role: {}", role);

        String redirectUrl = switch (role) {
            case ADMIN -> "/admin/home";
            case INSTRUCTOR -> "/instructor/home";
            case PARTICIPANT -> "/participant/home";
            default -> "/login";
        };

        if (message != null) {
            redirectAttributes.addFlashAttribute("message", message);
            if (messageType != null) {
                redirectAttributes.addFlashAttribute("messageType", messageType);
            }
        }

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
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        log.info("Register attempt with username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Login validation failed for username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());

            model.addAttribute("role", registerBaseUserRequest.getRole());
            return "register";
        }

        try {
            Optional<BaseUser> optionalBaseUser = baseUserService.registerBaseUser(registerBaseUserRequest);
            if (optionalBaseUser.isPresent()) {
                BaseUser baseUser = optionalBaseUser.get();
                log.info("Register successful for BaseUser ID: {}, Role: {}", baseUser.getId(), baseUser.getRole());

                session.setAttribute(IS_USER_VERIFIED, Boolean.TRUE);
                session.setAttribute(USER_ID, baseUser.getId());
                session.setAttribute(USER_ROLE, baseUser.getRole());
                log.info("BaseUser attributes set in session IS_USER_VERIFIED, USER_ID, USER_ROLE");

                redirectAttributes.addFlashAttribute("message", "Registration successful!");
                redirectAttributes.addFlashAttribute("messageType", "success");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Register failed for BaseUser username: {}, email: {}", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail());

                model.addAttribute("message", "Unexpected role type provided. Please contact support.");
                return "error";
            }
        } catch (UsernameAlreadyExistsException ex) {
            log.error("Registration failed: Username '{}' already exists", registerBaseUserRequest.getUsername(), ex);

            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("username", "Username.Invalid", "Username already exists.");
            return "register";
        } catch (EmailAlreadyExistsException ex) {
            log.error("Registration failed: Email '{}' already exists", registerBaseUserRequest.getEmail(), ex);

            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("email", "Email.Invalid", "Email already exists.");
            return "register";
        } catch (PasswordMismatchException ex) {
            log.error("Registration failed: Password mismatch for username '{}'", registerBaseUserRequest.getUsername(), ex);

            model.addAttribute("role", registerBaseUserRequest.getRole());
            bindingResult.rejectValue("confirmPassword", "ConfirmPassword.Invalid", "Password and confirm password must be same.");
            return "register";
        } catch (DataIntegrityViolationException ex) {
            log.error("Unexpected data integrity violation during registration for username '{}', email '{}'", registerBaseUserRequest.getUsername(), registerBaseUserRequest.getEmail(), ex);

            model.addAttribute("message", "An unexpected data integrity error occurred. Please contact support.");
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
            HttpSession session,
            RedirectAttributes redirectAttributes
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

                redirectAttributes.addFlashAttribute("message", "Login successful!");
                redirectAttributes.addFlashAttribute("messageType", "success");

                log.info("Redirecting to: /");
                return "redirect:/";
            } else {
                log.warn("Authentication failed for BaseUser identifier: {}", loginBaseUserRequest.getBaseUserIdentifier());
                model.addAttribute("error", "Invalid username/email or password");
                return "login";
            }
        } catch (BaseUserAccountNotActiveException ex) {
            log.warn("Exception occured: BaseUserAccountNotActiveException");

            model.addAttribute("message", ex.getMessage());
            return "error";
        } catch (AdminUserNotFoundException | InstructorUserNotFoundException | ParticipantUserNotFoundException ex) {
            log.warn("Exception occured: AdminUserNotFoundException | InstructorUserNotFoundException | ParticipantUserNotFoundException");

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
