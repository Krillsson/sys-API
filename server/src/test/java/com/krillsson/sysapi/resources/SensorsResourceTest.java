package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.sensors.SensorsInfo;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class SensorsResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new SensorsResource(provider))
            .build();

    @Test
    public void getSensorsappyPath() throws Exception {
        when(provider.sensorsInfo()).thenReturn(new SensorsInfo(new CpuHealth(new double[]{0, 0}, 1.2, 1000, 90), new HashMap<>(), new HealthData[]{new HealthData("temperature", 0.2, DataType.CELCIUS)}));

        final com.krillsson.sysapi.dto.sensors.SensorsInfo response = RESOURCES.getJerseyTest().target("/sensors")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.sensors.SensorsInfo.class);
        assertNotNull(response);
        assertEquals(response.getCpuHealth().getFanRpm(), 1000, 0);
    }

    @Test
    public void getSensorsSadPath() throws Exception {
        when(provider.sensorsInfo()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/sensors")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}
