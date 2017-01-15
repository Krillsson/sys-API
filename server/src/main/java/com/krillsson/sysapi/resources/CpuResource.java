package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuHealth;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.CentralProcessor;
import oshi.json.hardware.Sensors;
import oshi.json.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("processor")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private final OperatingSystem operatingSystem;
    private final Sensors sensors;
    private final CentralProcessor processor;
    private final InfoProvider provider;

    public CpuResource(OperatingSystem operatingSystem, Sensors sensors, CentralProcessor processor, InfoProvider provider) {
        this.operatingSystem = operatingSystem;
        this.sensors = sensors;
        this.processor = processor;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Cpu getRoot(@Auth UserConfiguration user) {
        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.cpuFanRpm();
        double fanPercent = provider.cpuFanPercent();
        if (temperature.length == 0) {
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return new Cpu(processor, operatingSystem.getProcessCount(), operatingSystem.getThreadCount(), new CpuHealth(temperature, sensors.getCpuVoltage(), fanRpm, fanPercent));
    }

}
