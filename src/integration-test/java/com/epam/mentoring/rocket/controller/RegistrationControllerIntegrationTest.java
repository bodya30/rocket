package com.epam.mentoring.rocket.controller;

import com.epam.mentoring.rocket.form.RegistrationForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerIntegrationTest {

    private static final String USER_FIRST_NAME = "firstName";
    private static final String USER_LAST_NAME = "lastName";
    private static final String USER_EMAIL = "bo.tkach@gmail.com";
    private static final String USER_PASSWORD = "passworD2";

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldSuccessfullyRegisterUser() throws Exception{
        mvc.perform(post("/registration/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRegistrationFormJson()))
                .andExpect(status().isNoContent());
    }

    private String createRegistrationFormJson() throws JsonProcessingException {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setFirstName(USER_FIRST_NAME);
        registrationForm.setLastName(USER_LAST_NAME);
        registrationForm.setEmail(USER_EMAIL);
        registrationForm.setPassword(USER_PASSWORD);
        registrationForm.setConfirmPassword(USER_PASSWORD);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(registrationForm);
    }

}