package com.largecorp.reponamegovernor.features.steps;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class World {
    String applicationType;
    String technology;
    String domain;
    String repositoryName;
}
