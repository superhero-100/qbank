package com.example.qbankapi.config;

import com.example.qbankapi.interceptor.AdminSessionValidationInterceptor;
import com.example.qbankapi.interceptor.SessionValidationInterceptor;
import com.example.qbankapi.interceptor.TeacherSessionValidationInterceptor;
import com.example.qbankapi.interceptor.UserSessionValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
@ComponentScan(basePackages = "com.example.qbankapi")
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

    private final SessionValidationInterceptor sessionValidationInterceptor;
    private final UserSessionValidationInterceptor userSessionValidationInterceptor;
    private final AdminSessionValidationInterceptor adminSessionValidationInterceptor;
    private final TeacherSessionValidationInterceptor teacherSessionValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor).addPathPatterns("/**").excludePathPatterns("/login","/register");
        registry.addInterceptor(adminSessionValidationInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(teacherSessionValidationInterceptor).addPathPatterns("/teacher/**");
        registry.addInterceptor(userSessionValidationInterceptor).addPathPatterns("/user/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/resources/static/");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

}
