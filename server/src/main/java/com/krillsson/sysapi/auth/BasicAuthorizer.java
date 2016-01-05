package com.krillsson.sysapi.auth;

import com.krillsson.sysapi.UserConfiguration;

import io.dropwizard.auth.Authorizer;

public class BasicAuthorizer implements Authorizer<UserConfiguration> {

    public static final String AUTHENTICATED_ROLE = "AUTHENTICATED";

    private final UserConfiguration userConfiguration;

    public BasicAuthorizer(UserConfiguration userConfiguration) {
        this.userConfiguration = userConfiguration;
    }

    @Override
    public boolean authorize(UserConfiguration userConfiguration, String s) {
        return userConfiguration.getName().equals(this.userConfiguration.getName()) && s.equals(AUTHENTICATED_ROLE);
    }
}
