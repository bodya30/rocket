package com.epam.mentoring.rocket.service.event.listener;

import com.epam.mentoring.rocket.service.event.RegistrationEmailEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.util.MimeTypeUtils.TEXT_HTML_VALUE;

@Component
public class RegistrationEmailEventListener implements ApplicationListener<RegistrationEmailEvent> {

    private static final String MESSAGE_SUBJECT = "Registration confirmation";
    private static final String EMAIL_TEMPLATE = "email-template.ftl";
    private static final String CONFIRMATION_LINK_KEY = "confirmationLink";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void onApplicationEvent(RegistrationEmailEvent event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(event.getEmail());
            messageHelper.setSubject(MESSAGE_SUBJECT);
            mimeMessage.setContent(getMessage(event), TEXT_HTML_VALUE);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getMessage(RegistrationEmailEvent event) {
        try {
            Template emailTemplate = freemarkerConfig.getTemplate(EMAIL_TEMPLATE);
            Map<String, String> model = singletonMap(CONFIRMATION_LINK_KEY, event.getConfirmationUrl());
            String htmlMessage = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate, model);
            return htmlMessage;
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new IllegalStateException("Email template not valid", e);
        }
    }
}
