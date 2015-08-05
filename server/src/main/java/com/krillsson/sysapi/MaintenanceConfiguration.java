package com.krillsson.sysapi;

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
    private String sigarLocation;

    public UserConfiguration getUser() {
        return user;
    }

    public String getSigarLocation() {
        return sigarLocation;
    }
}
