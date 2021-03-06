package com.largecorp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.largecorp.model.NameChangeEvent;
import com.largecorp.service.RepositoryCheckerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(RepositoryValidationController.class)
public class RepositoryValidationControllerTest {

    @MockBean
    RepositoryCheckerService repositoryCheckerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void itShouldAcceptOnAGoodPayload() throws Exception {
        String repositoryName = "org/microserivce-spring-finance-tickscraper";
        NameChangeEvent event = NameChangeEvent.builder()
            .repositoryName(repositoryName).build();
        String jsonBody = serialiseEventToJson(event);

        mockMvc.perform(post("/repository/name/" + repositoryName)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
            status().is(202)
        );
        verify(repositoryCheckerService).asynchronouslyCheckName(event);
    }

    @Test
    public void itShouldRejectABadPayload() throws Exception {
        String repositoryName = "org/microserivce-spring-finance-tickscraper";

        mockMvc.perform(post("/repository/name/" + repositoryName)
            .content("{BROKEN")
            .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
            status().is(400)
        );
        verify(repositoryCheckerService, never()).asynchronouslyCheckName(any(NameChangeEvent.class));
    }

    private String serialiseEventToJson(NameChangeEvent event) throws JsonProcessingException {
        ObjectMapper myMapper = new ObjectMapper();
        myMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = myMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(event);
    }

}