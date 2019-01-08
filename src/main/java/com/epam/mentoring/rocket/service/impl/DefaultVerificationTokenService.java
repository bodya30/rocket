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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Profile("jdbc")
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

    @Value("${verification.token.url}")
    private String verificationUrl;

    @Value("${verification.token.request.parameter.name}")
    private String tokenParameter;

    @Override
    public Optional<VerificationToken> getTokenByTokenString(String token) {
        return Optional.ofNullable(tokenDao.getTokenByTokenString(token)).map(this::setUser);
    }

    private VerificationToken setUser(VerificationToken token) {
        User user = userDao.getUserById(token.getUser().getId());
        token.setUser(user);
        return token;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // do not invoke method inside transaction
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

    @Override
    public Optional<VerificationToken> getTokenByUser(User user) {
        return Optional.ofNullable(tokenDao.getTokenByUser(user)).map(this::setUser);
    }

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
    public void removeTokenForUser(User user) {
        tokenDao.removeTokenForUser(user);
    }

    public VerificationTokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(VerificationTokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
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
}
