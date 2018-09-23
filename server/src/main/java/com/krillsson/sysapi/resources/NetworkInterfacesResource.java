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
import com.krillsson.sysapi.core.domain.network.NetworkInterfacesMapper;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.NetworkMetrics;
import com.krillsson.sysapi.dto.history.HistoryEntry;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("nics")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkInterfacesResource {

    private final NetworkMetrics infoProvider;
    private final MetricsHistoryManager historyManager;

    public NetworkInterfacesResource(NetworkMetrics infoProvider, MetricsHistoryManager historyManager) {
        this.infoProvider = infoProvider;
        this.historyManager = historyManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.network.NetworkInterface> networkInterfaces(@Auth UserConfiguration user) {
        return NetworkInterfacesMapper.INSTANCE.map(infoProvider.networkInterfaces());
    }

    @GET
    @Path("loads")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.network.NetworkInterfaceLoad> networkInterfaceLoads(@Auth UserConfiguration user) {
        return NetworkInterfacesMapper.INSTANCE.mapLoads(infoProvider.networkInterfaceLoads());
    }

    @GET
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.network.NetworkInterface networkInterfaceById(@Auth UserConfiguration user, @PathParam("id") String id) {
        return NetworkInterfacesMapper.INSTANCE.map(infoProvider.networkInterfaceById(id)
                                                            .orElseThrow(() -> new WebApplicationException(NOT_FOUND)));
    }

    @GET
    @Path("loads/{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.network.NetworkInterfaceLoad networkInterfaceLoadById(@Auth UserConfiguration user, @PathParam("id") String id) {
        return NetworkInterfacesMapper.INSTANCE.map(infoProvider.networkInterfaceLoadById(id)
                                                            .orElseThrow(() -> new WebApplicationException(NOT_FOUND)));
    }

    @GET
    @Path("loads/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<HistoryEntry<List<com.krillsson.sysapi.dto.network.NetworkInterfaceLoad>>> getLoadHistory(@Auth UserConfiguration user, @QueryParam("fromDate") LocalDateTime fromDate, @QueryParam("toDate") LocalDateTime toDate) {
        return NetworkInterfacesMapper.INSTANCE.mapHistory(historyManager.networkInterfaceLoadHistory(fromDate, toDate));
    }

}
