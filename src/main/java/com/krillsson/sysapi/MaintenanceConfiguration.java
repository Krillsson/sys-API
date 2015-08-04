package com.krillsson.sysapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import com.krillsson.sysapi.representation.config.UserConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaintenanceConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private UserConfiguration user;

    public UserConfiguration getUser() {
        return user;
    }
}
