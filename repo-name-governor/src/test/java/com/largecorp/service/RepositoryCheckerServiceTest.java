package com.largecorp.service;

import com.google.common.collect.Lists;
import com.largecorp.client.GitHubIssueClient;
import com.largecorp.model.NameChangeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class RepositoryCheckerServiceTest {

    @Mock
    GitHubIssueClient client;

    @InjectMocks
    RepositoryCheckerService underTest = new RepositoryCheckerService();

    @Before
    public void setup() {
        underTest.setApprovedApplications(Lists.newArrayList("webapp"));
        underTest.setApprovedTechnologies(Lists.newArrayList("django"));
        underTest.setApprovedDomains(Lists.newArrayList("media"));
    }

    @Test
    public void aWellFormedNameShouldNotRaiseAnIssue() {
        NameChangeEvent event = NameChangeEvent.builder()
            .repositoryName("org/webapp-django-media-transcoding")
            .build();

        underTest.asynchronouslyCheckName(event);
        verify(client, never()).createIssue(anyString(), anyString());
    }
}