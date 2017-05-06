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
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfoMapper;
import com.krillsson.sysapi.dto.processes.Process;
import io.dropwizard.auth.Auth;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("storage")
@Produces(MediaType.APPLICATION_JSON)
public class DiskStoresResource {

    private final InfoProvider provider;

    public DiskStoresResource(InfoProvider provider) {
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.storage.StorageInfo getRoot(@Auth UserConfiguration user) {
        StorageInfo storageInfo = provider.storageInfo();
        return StorageInfoMapper.INSTANCE.map(storageInfo);
    }

    @GET
    @Path("{name}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.storage.DiskInfo getDiskByName(@PathParam("name") String name) {
        Optional<DiskInfo> disk = provider.getDiskInfoByName(name);
        if (!disk.isPresent()) {
            throw new WebApplicationException(String.format("No disk with name %s was found.", name), NOT_FOUND);
        }
        return StorageInfoMapper.INSTANCE.map(disk.get());
    }

}
