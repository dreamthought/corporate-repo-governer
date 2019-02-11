package com.largecorp.reponamegovernor.features;

import com.largecorp.client.GitHubIssueClient;
import com.largecorp.controller.RepositoryValidationController;
import com.largecorp.reponamegovernor.features.steps.World;
import com.largecorp.service.GitHubJWTProvider;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions for validating assertions about repository naming scenarios
 */
@ContextConfiguration(classes= RunCucumberTest.class, loader = SpringBootContextLoader.class)
@WebMvcTest(RepositoryValidationController.class)
public class NameGovernanceSteps implements En {

    @MockBean
    GitHubJWTProvider gitHubJWTProvider;

    @MockBean
    GitHubIssueClient gitHubIssueClient;

    // TODO encapsulate in world
    @Autowired
    MockMvc mockMvc;

    public NameGovernanceSteps(@Autowired World world) {

        Given(".*a user needs a new (\\w+)$", (String applicationType) -> {
            // Write code here that turns the phrase above into concrete actions
            world.setApplicationType(applicationType);
        });

        When("^a repository is created named (\\w+/[\\w+-]+)$", (String repositoryName) -> {
            // Write code here that turns the phrase above into concrete actions
            world.setRepositoryName( repositoryName);
            String[] constituents = repositoryName.split("/-/");
            if (constituents.length == 3) {
                world.setApplicationType(constituents[0]);
                world.setTechnology(constituents[1]);
                world.setDomain(constituents[2]);
            }
        });

        Then("an issue in github will not be created", () -> {
            String repository = world.getRepositoryName();
            String fixture = String.format("{ \"repositoryName\": \"%s\" }", repository);
            mockMvc.perform(
                post("/repository/name/" + repository)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(fixture))
                .andExpect(status().is(202)
            );
        });

    }
}
