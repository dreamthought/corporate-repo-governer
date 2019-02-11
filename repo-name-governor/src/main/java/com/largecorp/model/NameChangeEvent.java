package com.largecorp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameChangeEvent {
    // FIXME: this did previously have more parameters but it's been reduced
    // leaving it like this for extensibility and time pressure
    String repositoryName;
}
