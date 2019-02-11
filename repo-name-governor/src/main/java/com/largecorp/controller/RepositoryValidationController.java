package com.largecorp.controller;

import com.largecorp.model.NameChangeEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * This exposes a single responsibiliy API to check repository naming conformity with governance
 */
@RestController
@RequestMapping("/repository")
public class RepositoryValidationController {

    /**
     * Tests a name event. This is why I am leading more and more towards DRPC these days
     * FIXME: Forced RESTfulness
     */
    @PostMapping("/name/{repositoryName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkRepositoryCreatedEvent(@PathVariable("repositoryName") String repositoryName, @RequestBody NameChangeEvent body) {

    }
}
