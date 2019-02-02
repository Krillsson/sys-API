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
        LOGGER.warn("Unauthorized access attempt: {}",  credentials.toString());
        return Optional.empty();
    }
}
