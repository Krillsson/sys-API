package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphQLConfiguration {
    @JsonProperty
    private boolean enablePlayGround;

    public GraphQLConfiguration(boolean enablePlayGround) {
        this.enablePlayGround = enablePlayGround;
    }

    public GraphQLConfiguration() {
    }

    public boolean enableOhmJniWrapper() {
        return enablePlayGround;
    }
}
