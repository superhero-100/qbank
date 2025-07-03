package com.example.qbankapi.interceptor;

import com.example.qbankapi.entity.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;
import static com.example.qbankapi.interceptor.constant.Variable.USER_ROLE;

@Slf4j
@Component
public class ParticipantUserSessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Intercepting request URI: {}", request.getRequestURI());

        HttpSession session = request.getSession(false);

        if (session != null) {
            Object userId = session.getAttribute(USER_ID);
            Object userRole = session.getAttribute(USER_ROLE);

            log.debug("Session found. USER_ID={}, USER_ROLE={}", userId, userRole);

            if (BaseUser.Role.PARTICIPANT.equals(userRole)) {
                log.debug("Participant session validated");
                return true;
            } else {
                log.debug("Session exists but role is not PARTICIPANT: {}", userRole);
            }
        } else {
            log.debug("No session found");
        }

        response.sendRedirect(request.getContextPath() + "/login");
        log.debug("Redirecting to /login");
        return false;
    }

}
