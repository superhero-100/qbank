package com.example.qbankapi.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.xml.validation.Validator;


@Configuration
public class AppConfig {

    @Value("${app.security.password.strength}")
    private Integer strength;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }

}
