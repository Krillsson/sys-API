package com.krillsson.sysapi.resources;

import io.dropwizard.auth.Auth;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuInfo;
import com.krillsson.sysapi.domain.cpu.CpuTime;
import com.krillsson.sysapi.sigar.CpuSigar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource extends Resource {

    private CpuSigar cpuSigar;

    public CpuResource(CpuSigar cpuSigar) {
        this.cpuSigar = cpuSigar;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @Override
    public Cpu getRoot(@Auth UserConfiguration user) {
        List<CpuTime> cores = new ArrayList<>();
        cores.add(new CpuTime(0.1, 0.1, 0.0, 0.0, 0.8, 0.0));
        cores.add(new CpuTime(0.1, 0.1, 0.0, 0.0, 0.8, 0.0));
        Cpu cpu = new Cpu(new CpuInfo("", "", 2300, 1000, 2, 1, 2), 10, new CpuTime(0.1, 0.1, 0.0, 0.0, 0.8, 0.0), cores);
        return cpu;
        //return cpuSigar.getCpu();
    }

    @Path("{core}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @GET
    public CpuTime getCpuTimeByCore(@Auth UserConfiguration user, @PathParam("core") int core) {
        try {
            return cpuSigar.getCpuTimeByCoreIndex(core);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
