package com.epam.mentoring.rocket;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import static freemarker.template.Configuration.VERSION_2_3_22;

@SpringBootApplication
public class RocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    Configuration freemarkerConfig() {
        Configuration config = new Configuration(VERSION_2_3_22);
        config.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/static/template/");
        return config;
    }

}
