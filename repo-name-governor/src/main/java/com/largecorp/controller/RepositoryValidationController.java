package com.largecorp.controller;

import com.largecorp.model.NameChangeEvent;
import com.largecorp.service.RepositoryCheckerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * This exposes a single responsibiliy API to check repository naming conformity with governance
 */
@Api(
    value="Repository Name Govenance API",
    description = "Applies LargeCorp's Repository Naming Governance")
@RestController
@RequestMapping("/repository")
public class RepositoryValidationController {

    @Autowired
    RepositoryCheckerService repositoryCheckerService;

    /**
     * Tests a name event. This is why I am leading more and more towards DRPC these days
     * FIXME: Forced RESTfulness
     */
    @PostMapping("/name/{org}/{repository}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkRepositoryCreatedEvent(@PathVariable("org") String org, @PathVariable("repository") String repositoryName, @RequestBody NameChangeEvent body) {
        repositoryCheckerService.asynchronouslyCheckName(body);
    }
}
