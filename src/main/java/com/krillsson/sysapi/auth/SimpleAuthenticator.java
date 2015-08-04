package com.krillsson.sysapi.auth;


import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import com.krillsson.sysapi.representation.config.UserConfiguration;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, UserConfiguration> {

    private UserConfiguration userConfiguration;
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SimpleAuthenticator.class.getSimpleName());

    public SimpleAuthenticator(UserConfiguration userConfiguration) {this.userConfiguration = userConfiguration;}

    @Override
    public com.google.common.base.Optional<UserConfiguration> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        if(userConfiguration != null &&
                userConfiguration.getUsername().equals(credentials.getUsername()) &&
                userConfiguration.getPassword().equals(credentials.getPassword()))
        {
            return Optional.of(userConfiguration);
        }
        LOGGER.info("Unauthorized access attempt: " + credentials.toString());
        return Optional.absent();
    }
}
