package com.epam.mentoring.rocket.dao;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("springdata")
@Configuration
@EnableJpaRepositories("com.epam.mentoring.rocket.dao.impl")
public class PersistanceConfiguration {

}
