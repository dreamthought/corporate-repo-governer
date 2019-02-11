package com.largecorp.reponamegovernor.features;

import com.largecorp.controller.RepositoryValidationController;
import com.largecorp.reponamegovernor.features.steps.World;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Step definitions for validating assertions about repository naming scenarios
 */
@ContextConfiguration(classes= RunCucumberTest.class, loader = SpringBootContextLoader.class)
@WebMvcTest(RepositoryValidationController.class)
public class NameGovernanceSteps implements En {

    // TODO encapsulate in world
    @Autowired
    MockMvc mockMvc;
    public NameGovernanceSteps(@Autowired World world) {

        Given(".*a user needs a new (\\w+)$", (String applicationType) -> {
            // Write code here that turns the phrase above into concrete actions
            world.setApplicationType(applicationType);
        });

        When("^a repository is created named ([\\w+-]+)$", (String repositoryName) -> {
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
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });

    }
}
