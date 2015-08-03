package se.christianjensen.maintenance.representation.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UserConfiguration {
    @NotNull
    @JsonProperty
    private String username = "admin";

    private String password = "password";


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
