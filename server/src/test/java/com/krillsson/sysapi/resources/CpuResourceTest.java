package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class CpuResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new CpuResource(provider))
            .build();

    @Test
    public void getCpuHappyPath() throws Exception {
        oshi.hardware.CentralProcessor centralProcessor = mock(oshi.hardware.CentralProcessor.class);
        when(provider.cpuInfo()).thenReturn(new CpuInfo(centralProcessor, 4, 80,
                new CpuLoad(100, 0, coreLoads),
                new CpuHealth(new double[0], 120, 1000, 10)));

        final com.krillsson.sysapi.dto.cpu.CpuInfo response = RESOURCES.getJerseyTest().target("/cpu")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.cpu.CpuInfo.class);
        assertNotNull(response);
        assertEquals(response.getCpuLoad().getCpuLoadCountingTicks(), 100, 0);
    }

    @Test
    public void getCpuSadPath() throws Exception {
        when(provider.cpuInfo()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/cpu")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @Test
    public void getCountingTicksHappyPath() throws Exception {
        when(provider.systemCpuLoadTicks()).thenReturn(new long[]{1L, 1L, 1L, 1L});
        final long[] response = RESOURCES.getJerseyTest().target("/cpu/ticks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(long[].class);
        assertNotNull(response);
        assertArrayEquals(new long[]{1L, 1L, 1L, 1L}, response);
    }

    @Test
    public void getCountingTicksSadPath() throws Exception {
        when(provider.systemCpuLoadTicks()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/cpu/ticks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}
