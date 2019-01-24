package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("springdata")
public interface SpringDataVerificationTokenDao extends VerificationTokenDao, CrudRepository<VerificationToken, Long> {

}
