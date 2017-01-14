package com.krillsson.sysapi.auth;


import com.krillsson.sysapi.config.UserConfiguration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicAuthorizerTest {

    private final String correctUser = "user";
    private UserConfiguration userConfiguration;

    private BasicAuthorizer authorizer;

    @Before
    public void setUp() throws Exception {
        userConfiguration = new UserConfiguration(correctUser, "");
        authorizer = new BasicAuthorizer(userConfiguration);
    }

    @Test
    public void correctRoleAndName() throws Exception {
        assertTrue(authorizer.authorize(new UserConfiguration(correctUser, ""), BasicAuthorizer.AUTHENTICATED_ROLE));
    }

    @Test
    public void incorrectRole() throws Exception {
        assertFalse(authorizer.authorize(new UserConfiguration(correctUser, ""), "derp"));
    }

    @Test
    public void incorrectUser() throws Exception {
        assertFalse(authorizer.authorize(new UserConfiguration("derp", ""), BasicAuthorizer.AUTHENTICATED_ROLE));
    }
}