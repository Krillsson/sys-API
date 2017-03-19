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
package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Map;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class MetaInfoResource {

    private final String version;
    private final String[] endpoints;

    public MetaInfoResource(String version, String[] endpoints) {
        this.version = version;
        this.endpoints = endpoints;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Object getRoot(@Auth UserConfiguration user) {
        return new Object()
        {
            public String message = "Hello from System API v" + getVersion(user);
            public String[] endpoints = MetaInfoResource.this.endpoints;
        };
    }

    @Path("version")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public String getVersion(@Auth UserConfiguration user) {
        return version;
    }

}
