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
import com.krillsson.sysapi.core.domain.memory.GlobalMemoryMapper;
import com.krillsson.sysapi.core.history.HistoryManager;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import com.krillsson.sysapi.dto.processes.Memory;
import io.dropwizard.auth.Auth;
import oshi.hardware.GlobalMemory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Map;

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
public class MemoryResource {

    private final MemoryMetrics provider;
    private final HistoryManager historyManager;

    public MemoryResource(MemoryMetrics provider, HistoryManager historyManager) {
        this.provider = provider;
        this.historyManager = historyManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Memory getRoot(@Auth UserConfiguration user) {
        return GlobalMemoryMapper.INSTANCE.map(provider.globalMemory());
    }

    @GET
    @Path("history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Map<String, Memory> getLoadHistory(@Auth UserConfiguration user) {
        Map<LocalDateTime, GlobalMemory> history = historyManager.get(GlobalMemory.class);
        return GlobalMemoryMapper.INSTANCE.mapHistory(history);
    }

}
