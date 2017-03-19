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
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.network.NetworkInterfacesDataMapper;
import com.krillsson.sysapi.dto.network.NetworkInterfaceData;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("nics")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkInterfacesResource {

    private final InfoProvider infoProvider;

    public NetworkInterfacesResource(InfoProvider infoProvider) {
        this.infoProvider = infoProvider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public NetworkInterfaceData[] getNetworkInterfaces(@Auth UserConfiguration user) {
        return NetworkInterfacesDataMapper.INSTANCE.map(infoProvider.getAllNetworkInterfaces());
    }

    @GET
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public NetworkInterfaceData getNetworkInterface(@Auth UserConfiguration user, @PathParam("id") String id) {
        final Optional<com.krillsson.sysapi.core.domain.network.NetworkInterfaceData> networkInterface = infoProvider.getNetworkInterfaceById(id);
        if (!networkInterface.isPresent()) {
            throw new WebApplicationException(NOT_FOUND);
        }
        return NetworkInterfacesDataMapper.INSTANCE.map(networkInterface.get());
    }

}
