package com.krillsson.sysapi.representation.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UserConfiguration {
    @NotNull
    @JsonProperty
    private String username = "admin";

    @NotNull
    @JsonProperty
    private String password = "password";

    public UserConfiguration(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserConfiguration() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
