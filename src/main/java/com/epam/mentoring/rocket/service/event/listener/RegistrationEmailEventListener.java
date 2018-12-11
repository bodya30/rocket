package com.epam.mentoring.rocket.service.event.listener;

import com.epam.mentoring.rocket.service.event.RegistrationEmailEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEmailEventListener implements ApplicationListener<RegistrationEmailEvent> {

    @Override
    public void onApplicationEvent(RegistrationEmailEvent event) {
        // TODO: 11.12.2018 Send email
    }
}
