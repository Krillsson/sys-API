package com.krillsson.sysapi.resources;


import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.sigar.NetworkSigar;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("networks")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource extends Resource {
    private NetworkSigar networkSigar;

    public NetworkResource(NetworkSigar NetworkSigar) {
        this.networkSigar = NetworkSigar;
    }

    @GET
    @Override
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)

    public NetworkInfo getRoot(@Auth UserConfiguration user) {
        return networkSigar.getNetworkInfo();
    }

    @Path("{name}")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public NetworkInterfaceConfig getConfigById(@Auth UserConfiguration user, @PathParam("name") String name) {
        try {
            return networkSigar.getConfigById(name);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }

    @Path("{id}/speed")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public NetworkInterfaceSpeed getNetworkInterfaceSpeedById(@Auth UserConfiguration user, @PathParam("id") String id) {
        try {
            return networkSigar.getSpeed(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        } catch (InterruptedException e) {
            throw buildWebException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
