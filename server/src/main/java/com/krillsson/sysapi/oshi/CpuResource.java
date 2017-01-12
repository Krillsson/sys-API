package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.util.TemperatureUtils;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.CentralProcessor;
import oshi.json.hardware.Sensors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {
    private final Sensors sensors;
    private CentralProcessor processor;


    public CpuResource(Sensors sensors, CentralProcessor processor) {
        this.sensors = sensors;
        this.processor = processor;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Cpu getRoot(@Auth UserConfiguration user) {
        return new Cpu(processor, sensors);
    }
}
