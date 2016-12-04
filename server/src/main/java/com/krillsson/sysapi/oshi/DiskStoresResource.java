package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.hardware.HWDiskStore;
import oshi.hardware.PowerSource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("diskstores")
@Produces(MediaType.APPLICATION_JSON)
public class DiskStoresResource {
    private final HWDiskStore[] diskStores;

    public DiskStoresResource(HWDiskStore[] diskStores) {
        this.diskStores = diskStores;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public HWDiskStore[] getRoot(@Auth UserConfiguration user) {
        return diskStores;
    }
}
