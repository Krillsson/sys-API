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
package com.krillsson.sysapi.rest;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.domain.gpu.GpuInfoMapper;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.GpuMetrics;
import com.krillsson.sysapi.dto.history.HistoryEntry;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.List;

@Path("gpus")
@Produces(MediaType.APPLICATION_JSON)
public class GpuResource {

    private final GpuMetrics provider;
    private final MetricsHistoryManager historyManager;

    public GpuResource(GpuMetrics provider, MetricsHistoryManager historyManager) {
        this.provider = provider;
        this.historyManager = historyManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.gpu.Gpu> getRoot(@Auth UserConfiguration user) {
        return GpuInfoMapper.INSTANCE.mapGpus(provider.gpus());
    }

    @GET
    @Path("loads")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<com.krillsson.sysapi.dto.gpu.GpuLoad> getLoad(@Auth UserConfiguration user) {
        return GpuInfoMapper.INSTANCE.map(provider.gpuLoads());
    }

    @GET
    @Path("loads/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<HistoryEntry<List<com.krillsson.sysapi.dto.gpu.GpuLoad>>> getLoadHistory(@Auth UserConfiguration user, @QueryParam("fromDate") OffsetDateTime fromDate, @QueryParam("toDate") OffsetDateTime toDate) {
        return GpuInfoMapper.INSTANCE.mapHistory(historyManager.gpuLoadHistory(fromDate, toDate));
    }

}
