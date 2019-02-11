package com.largecorp.model;

import lombok.Builder;
import lombok.Data;

/**
 * A minimal DITO for GitHub Issue Creation
 */
@Data
@Builder
public class GitHubIssue {
    String title;
    String body;
}
