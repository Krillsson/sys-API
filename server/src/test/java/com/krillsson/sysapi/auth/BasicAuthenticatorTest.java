package com.krillsson.sysapi.auth;

import com.krillsson.sysapi.UserConfiguration;
import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class BasicAuthenticatorTest {

    private final String correctPw = "password";
    private final String correctUsr = "user";
    private UserConfiguration userConfiguration;
    private BasicAuthenticator basicAuthenticator;

    @Before
    public void setUp() throws Exception {
        userConfiguration = new UserConfiguration(correctUsr, correctPw);
        basicAuthenticator = new BasicAuthenticator(userConfiguration);
    }

    @Test
    public void correctUserNameAndPassword() throws Exception {
        Optional<UserConfiguration> authenticate = basicAuthenticator.authenticate(new BasicCredentials(correctUsr, correctPw));
        assertTrue(authenticate.isPresent());
    }

    @Test
    public void wrongUserNameAndPassword() throws Exception {
        Optional<UserConfiguration> authenticate = basicAuthenticator.authenticate(new BasicCredentials("Derp", "123"));
        assertFalse(authenticate.isPresent());
    }
}