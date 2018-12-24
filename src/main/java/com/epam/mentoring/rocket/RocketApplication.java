package com.epam.mentoring.rocket;

import freemarker.template.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static freemarker.template.Configuration.VERSION_2_3_22;

@SpringBootApplication
public class RocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }

    @Bean
    Configuration freemarkerConfig() {
        Configuration config = new Configuration(VERSION_2_3_22);
        config.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/static/template/");
        return config;
    }

}
