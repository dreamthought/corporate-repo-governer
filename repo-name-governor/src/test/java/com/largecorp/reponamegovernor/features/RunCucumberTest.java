package com.largecorp.reponamegovernor.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(Cucumber.class)
@SpringBootTest
@WebAppConfiguration
@Configuration
@ComponentScan("com.largecorp.*")
@CucumberOptions(plugin = {"pretty"})
public class RunCucumberTest {

}

