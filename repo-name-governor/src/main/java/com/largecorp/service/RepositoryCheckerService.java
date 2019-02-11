package com.largecorp.service;

import com.largecorp.client.GitHubIssueClient;
import com.largecorp.model.NameChangeEvent;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RepositoryCheckerService {

    @Autowired
    GitHubIssueClient gitHubIssueClient;

    @Value("#{'${governance.valid.applications}'.split(',')}")
    @Getter @Setter
    private List<String> approvedApplications;

    @Value("#{'${governance.valid.technologies}'.split(',')}")
    @Getter @Setter
    private List<String> approvedTechnologies;

    @Value("#{'${governance.valid.domains}'.split(',')}")
    @Getter @Setter
    private List<String> approvedDomains;

    private static final String VALID_STRUCTURE_REX = "^(\\w+)/(\\w+)-(\\w+)-(\\w+)-.*$";
    private static final Pattern VALID_STRUCTURE_PATTERN =
        Pattern.compile(VALID_STRUCTURE_REX);

    // TODO: needs the language to be far nicer and requires a content db
    private static final String ISSUE_TEXT =
        "Please address your violation of naming standards on %s";


    // TODO Add a dedicated thread pool
    @Async
    public void asynchronouslyCheckName(NameChangeEvent nameChangeEvent) {
        String repositoryName = nameChangeEvent.getRepositoryName();
        Boolean isLegal = checkName(repositoryName);

        if (!isLegal) {
            String issue = String.format(ISSUE_TEXT, repositoryName);
            // create an issue on the repository
            gitHubIssueClient.createIssue(repositoryName, issue);
        }
    }

    private Boolean checkName(String repositoryName) {
        Matcher m = VALID_STRUCTURE_PATTERN.matcher(repositoryName);
        if (!m.matches() && m.groupCount() != 4) {
            return false;
        }
        String organisation = m.group(1);

        // check application validatity
        String application = m.group(2);
        if (! approvedApplications.contains(application)){
            return false;
        }

        // check technology validity
        String technology = m.group(3);
        if (! approvedTechnologies.contains(technology)) {
            return false;
        }

        // check if approved business domain
        String domain = m.group(4);
        if (! approvedDomains.contains(domain)) {
            return false;
        }

        return true;
    }

}
