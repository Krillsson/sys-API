package se.christianjensen.maintenance.resources;


import com.sun.jersey.api.NotFoundException;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.network.NetworkInterfaceConfig;
import se.christianjensen.maintenance.sigar.NetworkMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("network")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource {
    private NetworkMetrics networkMetrics;

    public NetworkResource(NetworkMetrics NetworkMetrics) {
        this.networkMetrics = NetworkMetrics;
    }

    @GET
    public NetworkInfo getNetworkInfo() {
        return networkMetrics.getNetworkInfo();
    }

    @Path("{id}")
    @GET
    public NetworkInterfaceConfig getConfigById(@PathParam("id") String id) {
        try {
            return networkMetrics.getConfigById(id);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

}
