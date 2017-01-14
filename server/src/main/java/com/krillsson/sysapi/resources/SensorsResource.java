package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.HealthData;
import com.krillsson.sysapi.domain.SensorsData;
import com.krillsson.sysapi.core.InfoProvider;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.Sensors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorsResource {
    private final Sensors sensors;
    private InfoProvider provider;

    public SensorsResource(Sensors sensors, InfoProvider provider) {
        this.sensors = sensors;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public SensorsData getRoot(@Auth UserConfiguration user) {
        double[] cpuTemperatures = provider.cpuTemperatures();
        double cpuFanRpm = provider.cpuFanRpm();
        double cpuFanPercent = provider.cpuFanPercent();
        if(cpuTemperatures.length == 0){
            cpuTemperatures = new double[]{sensors.getCpuTemperature()};
        }
        List<HealthData> healthDatas = provider.healthData();
        return new SensorsData(cpuTemperatures, cpuFanRpm, cpuFanPercent, healthDatas);
    }
}
