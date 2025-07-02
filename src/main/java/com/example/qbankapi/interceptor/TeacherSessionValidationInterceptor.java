package com.example.qbankapi.interceptor;

import com.example.qbankapi.entity.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.BASE_USER_ID;
import static com.example.qbankapi.interceptor.constant.Variable.BASE_USER_ROLE;

@Slf4j
@Component
public class TeacherSessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Intercepting request URI: {}", request.getRequestURI());

        HttpSession session = request.getSession(false);

        if (session != null) {
            Object id = session.getAttribute(BASE_USER_ID);
            Object role = session.getAttribute(BASE_USER_ROLE);

            log.debug("Session found. BASE_USER_ID={}, BASE_USER_ROLE={}", id, role);

            if (BaseUser.Role.USER.equals(role)) {
                log.debug("Teacher session validated");
                return true;
            } else {
                log.debug("Session exists but role is not USER: {}", role);
            }
        } else {
            log.debug("No session found");
        }

        response.sendRedirect(request.getContextPath() + "/login");
        log.debug("Redirecting to /login");
        return false;
    }

}
