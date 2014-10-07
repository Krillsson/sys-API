package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.sigar.CpuSigar;
import se.christianjensen.maintenance.sigar.SigarWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private CpuSigar cpuSigar;

    public CpuResource(CpuSigar cpuSigar) {
    this.cpuSigar = cpuSigar;
    }



    @GET
    public org.hyperic.sigar.CpuInfo[] All(){return cpuSigar.getCpuInfo();
    }
}
