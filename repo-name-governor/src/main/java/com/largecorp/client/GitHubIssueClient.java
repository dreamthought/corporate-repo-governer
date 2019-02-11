package com.largecorp.client;

import com.largecorp.service.GitHubJWTProvider;
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

    @Autowired
    GitHubJWTProvider jwtProvider;


    // FIXME this should really be pulled from the API but skipping due to time
    private static final String ISSUE_URL_FORMAT = "https://api.github.com/repos/%s/issues";

    /**
     * Creates an issue posting to URL in line
     * with githubs provided contract
     * @param url
     * @param issue
     */
    public void createIssue(String repository, String issue) {
        String createUrl = String.format(ISSUE_URL_FORMAT, repository);
        RestTemplate rest = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        String jwt = jwtProvider.getJwt();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = rest.postForEntity(
            createUrl, request , String.class);
    }


}
