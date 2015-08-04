package com.krillsson.hwapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import com.krillsson.hwapi.representation.config.UserConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaintenanceConfiguration extends Configuration {

    @NotNull
    @JsonProperty
    private String sigarLocation;

    @Valid
    @NotNull
    @JsonProperty
    private UserConfiguration user;

    public String getSigarLocation() {
        return sigarLocation;
    }

    public UserConfiguration getUser() {
        return user;
    }
}
