package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import com.epam.mentoring.rocket.service.event.RegistrationEmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class DefaultVerificationTokenService implements VerificationTokenService {

    @Autowired
    private VerificationTokenDao tokenDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Value("${verification.token.expiration.minutes}")
    private int expirationDateInMinutes;

    @Override
    public VerificationToken getByToken(String token) {
        VerificationToken verificationToken = tokenDao.getByToken(token);
        User user = userDao.getUserById(verificationToken.getUser().getId());
        verificationToken.setUser(user);
        return verificationToken;
    }

    @Override
    public void sendTokenToUser(User user) {
        VerificationToken token = createTokenForUser(user);
        // TODO: 12.12.2018 App URL ????
//        eventPublisher.publishEvent(new RegistrationEmailEvent(user.getEmail(), ));
    }

    private VerificationToken createTokenForUser(User user) {
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(calculateExpiryDate());
        return tokenDao.insertToken(token);
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expirationDateInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationTokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(VerificationTokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }
}
