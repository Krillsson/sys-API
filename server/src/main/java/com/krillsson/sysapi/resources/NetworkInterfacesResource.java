package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.domain.network.NetworkInterfacesData;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.HardwareAbstractionLayer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("networkinterfaces")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkInterfacesResource {

    private final HardwareAbstractionLayer hal;

    public NetworkInterfacesResource(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public NetworkInterfacesData getRoot(@Auth UserConfiguration user) {
        return new NetworkInterfacesData(hal.getNetworkIFs(), java.lang.System.currentTimeMillis());
    }

}
