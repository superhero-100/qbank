package com.example.qbankapi.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.IS_USER_VERIFIED;

@Slf4j
@Component
public class UserSessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Intercepting request URI: {}", request.getRequestURI());

        HttpSession session = request.getSession(true);

        if (session != null) {
            Object isUserVerified = session.getAttribute(IS_USER_VERIFIED);

            log.debug("Session found. IS_USER_VERIFIED={}", isUserVerified);

            if (Boolean.TRUE.equals(session.getAttribute(IS_USER_VERIFIED))) {
                log.debug("Request session validated");
                return true;
            }
        } else {
            log.debug("No session found");
        }

        response.sendRedirect(request.getContextPath() + "/login");
        log.debug("Redirecting to /login");
        return false;
    }

}
