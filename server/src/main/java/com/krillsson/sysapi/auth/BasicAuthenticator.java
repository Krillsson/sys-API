package com.krillsson.sysapi.auth;


import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.slf4j.Logger;

import com.krillsson.sysapi.UserConfiguration;

public class BasicAuthenticator implements Authenticator<BasicCredentials, UserConfiguration> {

    private UserConfiguration userConfiguration;
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BasicAuthenticator.class.getSimpleName());

    public BasicAuthenticator(UserConfiguration userConfiguration) {
        this.userConfiguration = userConfiguration;
    }

    @Override
    public Optional<UserConfiguration> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (userConfiguration != null &&
                userConfiguration.getUsername().equals(credentials.getUsername()) &&
                userConfiguration.getPassword().equals(credentials.getPassword())) {
            return Optional.of(userConfiguration);
        }
        LOGGER.warn("Unauthorized access attempt: " + credentials.toString());
        return Optional.absent();
    }
}
