package com.krillsson.sysapi.resources;

import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuTime;
import com.krillsson.sysapi.sigar.CpuSigar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cpus")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource extends Resource {

    private CpuSigar cpuSigar;

    public CpuResource(CpuSigar cpuSigar) {
    this.cpuSigar = cpuSigar;
    }

    @GET
    @Override
    public Cpu getRoot(@Auth UserConfiguration user) {
        return cpuSigar.getCpu();
    }

    @Path("{core}")
    @GET
    public CpuTime getCpuTimeByCore(@Auth UserConfiguration user, @PathParam("core") int core) {
        try {
            return cpuSigar.getCpuTimeByCoreIndex(core);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
