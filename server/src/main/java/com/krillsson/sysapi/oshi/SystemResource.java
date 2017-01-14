package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.system.JvmProperties;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.extension.InfoProvider;
import com.krillsson.sysapi.util.TemperatureUtils;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.*;
import oshi.json.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {

    private final TemperatureUtils temperatureUtils;
    private Sensors halSensors;
    private final OperatingSystem operatingSystem;
    private final ComputerSystem computerSystem;
    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final PowerSource[] powerSources;
    private final Sensors sensors;
    private final InfoProvider provider;

    public SystemResource(InfoProvider provider, TemperatureUtils temperatureUtils, Sensors halSensors, OperatingSystem operatingSystem, ComputerSystem computerSystem, CentralProcessor processor, GlobalMemory memory, PowerSource[] powerSources, Sensors sensors) {
        this.provider = provider;
        this.temperatureUtils = temperatureUtils;
        this.halSensors = halSensors;
        this.operatingSystem = operatingSystem;
        this.computerSystem = computerSystem;
        this.processor = processor;
        this.memory = memory;
        this.powerSources = powerSources;
        this.sensors = sensors;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public System getRoot(@Auth UserConfiguration user) {
        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.getCpuFanRpm();
        double fanPercent = provider.getCpuFanPercent();
        if(temperature.length == 0){
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return new System(
                operatingSystem,
                computerSystem,
                new Cpu(processor,sensors.getCpuVoltage(), fanPercent, fanRpm, temperature),
                memory,
                powerSources,
                sensors);
    }

    @GET
    @Path("jvm")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public JvmProperties getProperties(@Auth UserConfiguration user) {
        Properties p = java.lang.System.getProperties();
        return new JvmProperties(
                p.getProperty("java.home"),
                p.getProperty("java.class.path"),
                p.getProperty("java.vendor"),
                p.getProperty("java.vendor.url"),
                p.getProperty("java.version"),
                p.getProperty("os.arch"),
                p.getProperty("os.name"),
                p.getProperty("os.version"),
                p.getProperty("user.dir"),
                p.getProperty("user.home"),
                p.getProperty("user.name"));
    }
}
