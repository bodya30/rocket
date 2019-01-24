package com.epam.mentoring.rocket.dao;

import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;

public interface VerificationTokenDao {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    VerificationToken save(VerificationToken token);

    void removeByUser(User user);
}
