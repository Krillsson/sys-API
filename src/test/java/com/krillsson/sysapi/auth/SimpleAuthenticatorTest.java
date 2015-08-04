package com.krillsson.sysapi.auth;

import com.google.common.base.Optional;
import com.krillsson.sysapi.representation.config.UserConfiguration;
import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAuthenticatorTest {

    private final String correctPw = "password";
    private final String correctUsr = "user";
    private UserConfiguration userConfiguration;
    private SimpleAuthenticator simpleAuthenticator;

    @Before
    public void setUp() throws Exception {
        userConfiguration = new UserConfiguration(correctUsr, correctPw);
        simpleAuthenticator = new SimpleAuthenticator(userConfiguration);
    }

    @Test
    public void correctUserNameAndPassword() throws Exception {
        Optional<UserConfiguration> authenticate = simpleAuthenticator.authenticate(new BasicCredentials(correctUsr, correctPw));
        assertTrue(authenticate.isPresent());
    }

    @Test
    public void wrongUserNameAndPassword() throws Exception {
        Optional<UserConfiguration> authenticate = simpleAuthenticator.authenticate(new BasicCredentials("Derp", "123"));
        assertFalse(authenticate.isPresent());
    }
}