package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphQLPlayGroundConfiguration {
    @JsonProperty
    private boolean enabled;

    public GraphQLPlayGroundConfiguration(boolean enabled) {
        this.enabled = enabled;
    }

    public GraphQLPlayGroundConfiguration() {
    }

    public boolean enabled() {
        return enabled;
    }
}
