package com.krillsson.sysapi.resources;


import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.representation.config.UserConfiguration;
import com.krillsson.sysapi.representation.network.NetworkInfo;
import com.krillsson.sysapi.representation.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.representation.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.sigar.NetworkMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("network")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource extends Resource {
    private NetworkMetrics networkMetrics;

    public NetworkResource(NetworkMetrics NetworkMetrics) {
        this.networkMetrics = NetworkMetrics;
    }

    @GET
    @Override
    public NetworkInfo getRoot(@Auth UserConfiguration user) {
        return networkMetrics.getNetworkInfo();
    }

    @Path("{id}")
    @GET
    public NetworkInterfaceConfig getConfigById(@Auth UserConfiguration user, @PathParam("id") String id) {
        try {
            return networkMetrics.getConfigById(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }

    @Path("{id}/speed")
    @GET
    public NetworkInterfaceSpeed getNetworkInterfaceSpeedById(@Auth UserConfiguration user, @PathParam("id") String id) {
        try {
            return networkMetrics.getSpeed(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        } catch (InterruptedException e) {
            throw buildWebException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
