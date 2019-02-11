package com.largecorp.service;

import com.largecorp.client.GitHubIssueClient;
import com.largecorp.model.NameChangeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("async-disabled")
public class RepositoryCheckerServiceTest {

    @Autowired
    RepositoryCheckerService underTest;

    @Autowired
    GitHubIssueClient client;


    @Test
    public void aWellFormedNameShouldNotRaiseAnIssue() {
        NameChangeEvent event = NameChangeEvent.builder()
            .repositoryName("webapp-django-media-transcoding")
            .issueUrl("http://github.example.com/issues")
            .build();

        underTest.asynchronouslyCheckName(event);
    }
}