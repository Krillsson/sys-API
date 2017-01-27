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
import com.krillsson.sysapi.core.domain.system.Process;
import com.krillsson.sysapi.core.domain.system.ProcessesInfo;
import io.dropwizard.auth.Auth;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.stream.Collectors;

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessesResource {

    private final OperatingSystem operatingSystem;
    private GlobalMemory memory;

    public ProcessesResource(OperatingSystem operatingSystem, GlobalMemory memory) {
        this.operatingSystem = operatingSystem;
        this.memory = memory;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public ProcessesInfo getRoot(@Auth UserConfiguration user) {
        Process[] processes = Arrays.stream(operatingSystem.getProcesses(0, OperatingSystem.ProcessSort.NAME)).map(p -> new Process(p.getName(),
                p.getPath(),
                p.getState(),
                p.getProcessID(),
                p.getParentProcessID(),
                p.getThreadCount(),
                p.getPriority(),
                p.getVirtualSize(),
                p.getResidentSetSize(),
                100d * p.getResidentSetSize() / memory.getTotal(),
                p.getKernelTime(), p.getUserTime(),
                p.getUpTime(),
                100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                p.getStartTime(),
                p.getBytesRead(),
                p.getBytesWritten())).collect(Collectors.toList()).toArray(new Process[0]);
        return new ProcessesInfo(memory, processes);
    }
}
