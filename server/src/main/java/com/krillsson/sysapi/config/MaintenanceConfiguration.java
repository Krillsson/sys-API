package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaintenanceConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private UserConfiguration user;

    @Valid
    @JsonProperty
    private boolean forwardHttps;

    public UserConfiguration getUser() {
        return user;
    }

    public boolean forwardHttps() {
        return forwardHttps;
    }
}
