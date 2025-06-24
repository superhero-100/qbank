package com.example.qbankapi.interceptor;

import com.example.qbankapi.entity.constant.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.example.qbankapi.interceptor.constant.Variable.USER_ID;
import static com.example.qbankapi.interceptor.constant.Variable.USER_ROLE;

@Component
public class UserSessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session.getAttribute(USER_ID) != null || session.getAttribute(USER_ROLE) != null) {
            if (Role.USER.equals(session.getAttribute(USER_ROLE))) {
                return true;
            }
        }

        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login");
        return false;
    }

}
