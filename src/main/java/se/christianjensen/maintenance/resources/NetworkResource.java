package se.christianjensen.maintenance.resources;


import io.dropwizard.auth.Auth;
import se.christianjensen.maintenance.representation.internal.User;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceConfig;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceSpeed;
import se.christianjensen.maintenance.sigar.NetworkMetrics;

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
    public NetworkInfo getRoot(@Auth User user) {
        return networkMetrics.getNetworkInfo();
    }

    @Path("{id}")
    @GET
    public NetworkInterfaceConfig getConfigById(@Auth User user, @PathParam("id") String id) {
        try {
            return networkMetrics.getConfigById(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }

    @Path("{id}/speed")
    @GET
    public NetworkInterfaceSpeed getNetworkInterfaceSpeedById(@Auth User user, @PathParam("id") String id) {
        try {
            return networkMetrics.getSpeed(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        } catch (InterruptedException e) {
            throw buildWebException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
