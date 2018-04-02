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
import com.krillsson.sysapi.core.domain.drives.DriveMetricsMapper;
import com.krillsson.sysapi.core.history.HistoryManager;
import com.krillsson.sysapi.core.metrics.DriveMetrics;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("drives")
@Produces(MediaType.APPLICATION_JSON)
public class DrivesResource {

    private final DriveMetrics provider;
    private final HistoryManager historyManager;

    public DrivesResource(DriveMetrics provider, HistoryManager historyManager) {
        this.provider = provider;
        this.historyManager = historyManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.drives.Drive> getRoot(@Auth UserConfiguration user) {
        return DriveMetricsMapper.INSTANCE.map(provider.drives());
    }

    @GET
    @Path("{name}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.drives.Drive getDiskByName(@PathParam("name") String name) {
        return DriveMetricsMapper.INSTANCE.map(provider.driveByName(name)
                                                       .orElseThrow(() -> new WebApplicationException(String.format(
                                                               "No disk with name %s was found.",
                                                               name
                                                       ), NOT_FOUND)));
    }

    @GET
    @Path("loads/{name}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.drives.DriveLoad getDiskLoadByName(@PathParam("name") String name) {
        return DriveMetricsMapper.INSTANCE.map(provider.driveLoadByName(name)
                                                       .orElseThrow(() -> new WebApplicationException(String.format(
                                                               "No disk with name %s was found.",
                                                               name
                                                       ), NOT_FOUND)));
    }

    @GET
    @Path("loads")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.drives.DriveLoad> getLoads(@Auth UserConfiguration user) {
        return DriveMetricsMapper.INSTANCE.mapLoads(provider.driveLoads());
    }

    @GET
    @Path("loads/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Map<String, List<com.krillsson.sysapi.dto.drives.DriveLoad>> getLoadHistory(@Auth UserConfiguration user) {
        Map<LocalDateTime, List<com.krillsson.sysapi.dto.drives.DriveLoad>> history = historyManager.get(com.krillsson.sysapi.core.domain.drives.DriveLoad.class);
        return DriveMetricsMapper.INSTANCE.mapLoadHistory(history);
    }
}
