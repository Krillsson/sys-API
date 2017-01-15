package com.krillsson.sysapi.auth;


import com.krillsson.sysapi.config.UserConfiguration;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;

import java.util.Optional;

public class BasicAuthenticator implements Authenticator<BasicCredentials, UserConfiguration> {

    private UserConfiguration userConfiguration;
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BasicAuthenticator.class.getSimpleName());

    public BasicAuthenticator(UserConfiguration userConfiguration) {
        this.userConfiguration = userConfiguration;
    }

    @Override
    public java.util.Optional<UserConfiguration> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (userConfiguration != null &&
                userConfiguration.getUsername().equals(credentials.getUsername()) &&
                userConfiguration.getPassword().equals(credentials.getPassword())) {
            return Optional.of(userConfiguration);
        }
        LOGGER.warn("Unauthorized access attempt: " + credentials.toString());
        return Optional.empty();
    }
}
