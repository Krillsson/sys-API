package com.krillsson.sysapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;

import javax.validation.constraints.NotNull;

public class UserConfiguration implements Principal {
    @NotNull
    @JsonProperty
    private String username;

    @NotNull
    @JsonProperty
    private String password;

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

    @Override
    public String getName() {
        return username;
    }
}
