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
import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper;
import com.krillsson.sysapi.core.history.HistoryManager;
import com.krillsson.sysapi.core.metrics.CpuMetrics;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Map;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private final CpuMetrics provider;
    private final HistoryManager historyManager;

    public CpuResource(CpuMetrics provider, HistoryManager historyManager) {
        this.provider = provider;
        this.historyManager = historyManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.cpu.CpuInfo getRoot(@Auth UserConfiguration user) {
        return CpuInfoMapper.INSTANCE.map(provider.cpuInfo());
    }


    @GET
    @Path("load")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.cpu.CpuLoad getLoad(@Auth UserConfiguration user) {
        return CpuInfoMapper.INSTANCE.map(provider.cpuLoad());
    }

    @GET
    @Path("load/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Map<String, com.krillsson.sysapi.dto.cpu.CpuLoad> getLoadHistory(@Auth UserConfiguration user) {
        Map<LocalDateTime, com.krillsson.sysapi.core.domain.cpu.CpuLoad> history = historyManager.get(com.krillsson.sysapi.core.domain.cpu.CpuLoad.class);
        return CpuInfoMapper.INSTANCE.mapLoadHistory(history);
    }
}
