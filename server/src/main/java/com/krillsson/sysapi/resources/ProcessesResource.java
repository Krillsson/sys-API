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
import com.krillsson.sysapi.core.domain.processes.ProcessInfoMapper;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.dto.processes.ProcessInfo;
import com.krillsson.sysapi.dto.processes.Process;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessesResource {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProcessesResource.class);

    private final InfoProvider provider;

    public ProcessesResource(InfoProvider provider) {
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public ProcessInfo getRoot(@Auth UserConfiguration user, @QueryParam("sortBy") Optional<String> processSort, @QueryParam("limit") Optional<Integer> limit) {
        OperatingSystem.ProcessSort sortBy = OperatingSystem.ProcessSort.NAME;
        if (processSort.isPresent()) {
            String method = processSort.get().toUpperCase();
            try {
                sortBy = OperatingSystem.ProcessSort.valueOf(method);
            } catch (IllegalArgumentException e) {
                String validOptions = Arrays.stream(OperatingSystem.ProcessSort.values()).map(Enum::name).collect(Collectors.joining(", ", "Valid options are: ", "."));
                LOGGER.error("No process sort method of type {} was found. {}", method, validOptions);
                throw new WebApplicationException(String.format("No process sort method of type %s was found. %s", method, validOptions),
                        Response.Status.BAD_REQUEST);
            }
        }
        Integer theLimit = limit.orElse(0);
        if(theLimit < 0){
            String message = String.format("limit cannot be negative (%d)", theLimit);
            LOGGER.error(message);
            throw new WebApplicationException(message,Response.Status.BAD_REQUEST);
        }

        ProcessesInfo value = provider.processesInfo(sortBy, theLimit);
        return ProcessInfoMapper.INSTANCE.map(value);
    }

    @GET
    @Path("{pid}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Process getProcessByPid(@PathParam("pid") int pid) {
        Optional<com.krillsson.sysapi.core.domain.processes.Process> process = provider.getProcessByPid(pid);
        if (!process.isPresent()) {
            throw new WebApplicationException(String.format("No process with PID %d was found.", pid), NOT_FOUND);
        }
        return ProcessInfoMapper.INSTANCE.map(process.get());
    }
}
