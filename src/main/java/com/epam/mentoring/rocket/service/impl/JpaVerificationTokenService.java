package com.epam.mentoring.rocket.service.impl;

import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import com.epam.mentoring.rocket.service.VerificationTokenService;
import com.epam.mentoring.rocket.service.event.RegistrationEmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Profile("jpa")
@Service
@Transactional
public class JpaVerificationTokenService implements VerificationTokenService {

    @Autowired
    private VerificationTokenDao tokenDao;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Value("${verification.token.expiration.minutes}")
    private int expirationDateInMinutes;

    @Value("${verification.token.url}")
    private String verificationUrl;

    @Value("${verification.token.request.parameter.name}")
    private String tokenParameter;

    @Override
    public VerificationToken insertTokenForUser(User user) {
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

    @Override
    public Optional<VerificationToken> getTokenByTokenString(String token) {
        return Optional.ofNullable(tokenDao.getTokenByTokenString(token));
    }

    @Override
    public Optional<VerificationToken> getTokenByUser(User user) {
        return Optional.ofNullable(tokenDao.getTokenByUser(user));
    }

    @Override
    public void sendTokenToUser(User user, String appUrl) {
        Optional<VerificationToken> tokenOptional = getTokenByUser(user);
        if (tokenOptional.isPresent()) {
            VerificationToken token = tokenOptional.get();
            String tokenString = token.getToken();
            String confirmationUrl = buildConfirmationUrl(appUrl, tokenString);
            RegistrationEmailEvent event = new RegistrationEmailEvent(user.getEmail(), confirmationUrl);
            eventPublisher.publishEvent(event);
        } else {
            throw new IllegalStateException("No token for user found");
        }
    }

    private String buildConfirmationUrl(String appUrl, String token) {
        StringBuilder builder = new StringBuilder();
        builder.append(appUrl);
        builder.append(verificationUrl);
        builder.append("?" + tokenParameter + "=");
        builder.append(token);
        return builder.toString();
    }

    public VerificationTokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(VerificationTokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }
}
