package com.epam.mentoring.rocket.service.event.listener;

import com.epam.mentoring.rocket.service.event.RegistrationEmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEmailEventListener implements ApplicationListener<RegistrationEmailEvent> {

    private static final String MESSAGE_SUBJECT = "Registration confirmation";
    private static final String MESSAGE_TEXT_TEMPLATE = "Please confirm your registration %s";

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void onApplicationEvent(RegistrationEmailEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(MESSAGE_SUBJECT);
        message.setText(String.format(MESSAGE_TEXT_TEMPLATE, event.getConfirmationUrl()));
        emailSender.send(message);
    }
}
