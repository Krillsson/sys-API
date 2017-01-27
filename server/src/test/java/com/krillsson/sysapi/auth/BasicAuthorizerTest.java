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