package com.largecorp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Profile("!async-disabled") // for testing
/**
 * An application intended to validate repository names in accordance with internal governance at LargeCorp Inc
 */
public class RepoNameGovernorApp {

	public static void main(String[] args) {
		SpringApplication.run(RepoNameGovernorApp.class, args);
	}

}

