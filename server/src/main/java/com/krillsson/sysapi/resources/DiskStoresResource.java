package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.storage.HWDisk;
import com.krillsson.sysapi.domain.storage.StorageInfo;
import com.krillsson.sysapi.core.InfoProvider;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.HWDiskStore;
import oshi.json.hardware.HWPartition;
import oshi.json.software.os.FileSystem;
import oshi.json.software.os.OSFileStore;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("disks")
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
    public StorageInfo getRoot(@Auth UserConfiguration user) {
        List<HWDisk> HWDisks = new ArrayList<>();
        for (HWDiskStore diskStore : diskStores) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            HWDisks.add(new HWDisk(diskStore, provider.diskHealth(associatedFileStore != null ? associatedFileStore.getMount() : "", diskStore), associatedFileStore));
        }
        return new StorageInfo(HWDisks.toArray(/*type reference*/new HWDisk[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors());
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
