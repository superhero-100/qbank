package com.example.qbankapi.config;

import com.example.qbankapi.interceptor.AdminUserSessionValidationInterceptor;
import com.example.qbankapi.interceptor.ParticipantUserSessionValidationInterceptor;
import com.example.qbankapi.interceptor.InstructorUserSessionValidationInterceptor;
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

    private final UserSessionValidationInterceptor userSessionValidationInterceptor;
    private final AdminUserSessionValidationInterceptor adminUserSessionValidationInterceptor;
    private final InstructorUserSessionValidationInterceptor instructorUserSessionValidationInterceptor;
    private final ParticipantUserSessionValidationInterceptor participantUserSessionValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userSessionValidationInterceptor).addPathPatterns("/**").excludePathPatterns("/login","/register");
        registry.addInterceptor(adminUserSessionValidationInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(instructorUserSessionValidationInterceptor).addPathPatterns("/instructor/**");
        registry.addInterceptor(participantUserSessionValidationInterceptor).addPathPatterns("/participant/**");
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
