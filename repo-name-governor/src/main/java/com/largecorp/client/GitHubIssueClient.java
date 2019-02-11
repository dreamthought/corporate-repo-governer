package com.largecorp.client;

import com.largecorp.model.GitHubIssue;
import com.largecorp.service.GitHubJWTProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Used for creating issues in github
 *
 */
@Service
public class GitHubIssueClient {

    Logger logger = LoggerFactory.getLogger(GitHubIssueClient.class);

    @Autowired
    GitHubJWTProvider jwtProvider;



    private static final String TITLE = "REPOSITORY STANDARDS VIOLATION DETECTED";

    // FIXME this should really be pulled from the API but skipping due to time
    private static final String ISSUE_URL_FORMAT = "https://api.github.com/repos/%s/issues";

    /**
     * Creates an issue posting to URL in line
     * with githubs provided contract
     * @param repository name
     * @param issueDetail to be posted
     */
    public void createIssue(String repository, String issueDetail) {
        String createUrl = String.format(ISSUE_URL_FORMAT, repository);
        GitHubIssue issue = GitHubIssue.builder().title(TITLE)
            .body(issueDetail).build();

        sendIssue(createUrl, issue);
    }

    private void sendIssue(String createUrl, GitHubIssue issue) {
        logger.info("Creating an issue at url:", createUrl);
        RestTemplate rest = new RestTemplate();
        HttpEntity<GitHubIssue> request = makeRequest(issue);

        ResponseEntity<String> response = rest.postForEntity(
            createUrl, request , String.class);
    }

    private HttpEntity<GitHubIssue> makeRequest(GitHubIssue issue) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String jwt = jwtProvider.getJwt();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new HttpEntity<GitHubIssue>(issue, httpHeaders);
    }


}
