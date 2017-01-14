package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.storage.Disk;
import com.krillsson.sysapi.domain.storage.Storage;
import com.krillsson.sysapi.extension.InfoProvider;
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
    public Storage getRoot(@Auth UserConfiguration user) {
        List<Disk> disks = new ArrayList<>();
        for (HWDiskStore diskStore : diskStores) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            disks.add(new Disk(diskStore, provider.diskHealth(associatedFileStore != null ? associatedFileStore.getMount() : "", diskStore), associatedFileStore));
        }
        return new Storage(disks.toArray(/*type reference*/new Disk[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors());
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
