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
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfoMapper;
import io.dropwizard.auth.Auth;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("storage")
@Produces(MediaType.APPLICATION_JSON)
public class DiskStoresResource {

    private final HWDiskStore[] diskStores;
    private final FileSystem fileSystem;
    private final InfoProvider provider;

    public DiskStoresResource(HWDiskStore[] diskStores, FileSystem fileSystem, InfoProvider provider) {
        this.diskStores = diskStores;
        this.fileSystem = fileSystem;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.storage.StorageInfo getRoot(@Auth UserConfiguration user) {
        List<DiskInfo> DiskInfos = new ArrayList<>();
        for (HWDiskStore diskStore : diskStores) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            String name = associatedFileStore != null ? associatedFileStore.getMount() : "";
            DiskInfos.add(new DiskInfo(diskStore, provider.diskHealth(name), associatedFileStore));
        }
        StorageInfo storageInfo = new StorageInfo(DiskInfos.toArray(/*type reference*/new DiskInfo[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors(), System.currentTimeMillis());
        return StorageInfoMapper.INSTANCE.map(storageInfo);
    }

    private OSFileStore findAssociatedFileStore(HWDiskStore diskStore) {
        for (OSFileStore osFileStore : Arrays.asList(fileSystem.getFileStores())) {
            for (HWPartition hwPartition : Arrays.asList(diskStore.getPartitions())) {
                if (osFileStore.getUUID().equals(hwPartition.getUuid())) {
                    return osFileStore;
                }
            }
        }
        return null;
    }

}
