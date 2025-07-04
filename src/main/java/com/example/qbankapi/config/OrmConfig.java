package com.example.qbankapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class OrmConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Value("${spring.hibernate.entity.base_package.path}")
    private String entityBasePackagePath;

    @Value("${spring.hibernate.dialect}")
    private String dialect;

    @Value("${spring.hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${spring.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.hibernate.format_sql}")
    private String formatSql;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName(dbDriver);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(entityBasePackagePath);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties props = new Properties();
        props.put("hibernate.dialect", dialect);
        props.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        props.put("hibernate.show_sql", showSql);
        props.put("hibernate.format_sql", formatSql);
        entityManagerFactoryBean.setJpaProperties(props);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
