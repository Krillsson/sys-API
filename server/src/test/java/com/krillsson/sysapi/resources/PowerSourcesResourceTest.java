package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import oshi.hardware.PowerSource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class PowerSourcesResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new PowerSourcesResource(provider))
            .build();

    @Test
    public void getPowerSourceHappyPath() throws Exception {
        PowerSource powerSource = mock(PowerSource.class);
        when(powerSource.getName()).thenReturn("battery");
        when(powerSource.getRemainingCapacity()).thenReturn(0.1);
        when(powerSource.getTimeRemaining()).thenReturn(100.0);
        when(provider.powerSources()).thenReturn(new PowerSource[]{powerSource});

        final com.krillsson.sysapi.dto.power.PowerSource[] response = RESOURCES.getJerseyTest().target("/powersources")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.power.PowerSource[].class);
        assertNotNull(response);
        assertEquals(1, response.length);
        assertEquals("battery", response[0].getName());
    }

    @Test
    public void getPowerSourceSadPath() throws Exception {
        when(provider.powerSources()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/powersources")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}