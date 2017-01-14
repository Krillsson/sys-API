package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.extension.InfoProvider;
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
    private InfoProvider provider;

    public CpuResource(Sensors sensors, CentralProcessor processor, InfoProvider provider) {
        this.sensors = sensors;
        this.processor = processor;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Cpu getRoot(@Auth UserConfiguration user) {
        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.getCpuFanRpm();
        double fanPercent = provider.getCpuFanPercent();
        if(temperature.length == 0){
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return new Cpu(processor,sensors.getCpuVoltage(), fanPercent, fanRpm, temperature);
    }
}
