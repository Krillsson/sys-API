/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.auth;

import com.krillsson.sysapi.config.UserConfiguration;
import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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