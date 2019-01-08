package com.epam.mentoring.rocket.service.impl;

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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public abstract class AbstractVerificationTokenService implements VerificationTokenService {

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
    public void sendTokenToUser(User user, String appUrl) {
        Optional<VerificationToken> tokenOptional = getTokenByUser(user);
        if (tokenOptional.isPresent()) {
            VerificationToken token = tokenOptional.get();
            String tokenString = token.getToken();
            String confirmationUrl = buildConfirmationUrl(appUrl, tokenString);
            RegistrationEmailEvent event = new RegistrationEmailEvent(user.getEmail(), confirmationUrl);
            getEventPublisher().publishEvent(event);
        } else {
            throw new IllegalStateException("No token for user found");
        }
    }

    private String buildConfirmationUrl(String appUrl, String token) {
        StringBuilder builder = new StringBuilder();
        builder.append(appUrl);
        builder.append(verificationUrl);
        builder.append("?");
        builder.append(tokenParameter);
        builder.append("=");
        builder.append(token);
        return builder.toString();
    }

    @Override
    public void removeTokenForUser(User user) {
        getTokenDao().removeTokenForUser(user);
    }

    public VerificationTokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(VerificationTokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public int getExpirationDateInMinutes() {
        return expirationDateInMinutes;
    }

    public void setExpirationDateInMinutes(int expirationDateInMinutes) {
        this.expirationDateInMinutes = expirationDateInMinutes;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public void setVerificationUrl(String verificationUrl) {
        this.verificationUrl = verificationUrl;
    }

    public String getTokenParameter() {
        return tokenParameter;
    }

    public void setTokenParameter(String tokenParameter) {
        this.tokenParameter = tokenParameter;
    }
}
