package se.christianjensen.maintenance.resources;


import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.sigar.NetworkMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("network")
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource {
    private NetworkMetrics NetworkMetrics;

    public NetworkResource(NetworkMetrics NetworkMetrics) {
        this.NetworkMetrics = NetworkMetrics;
    }

    @GET
    public NetworkInfo all(){
        return NetworkMetrics.getNetworkInfo();
    }
}
