package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import oshi.hardware.Display;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class GpuResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new GpuResource(provider))
            .build();

    @Test
    public void getGpuInfoHappyPath() throws Exception {
        GpuInfo gpuInfo = mock(GpuInfo.class);
        when(gpuInfo.getDisplays()).thenReturn(new Display[0]);
        when(gpuInfo.getGpus()).thenReturn(new Gpu[]{new Gpu("Nvidia", "GTX 970", 1000.0, 400.0, new GpuHealth(0.0, 0.0, 40, 0.5, 0.5))});
        when(provider.gpuInfo()).thenReturn(gpuInfo);
        final com.krillsson.sysapi.dto.gpu.GpuInfo response = RESOURCES.getJerseyTest().target("/gpus")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.gpu.GpuInfo.class);
        assertNotNull(response);
        assertEquals("Nvidia", gpuInfo.getGpus()[0].getVendor());
    }

    @Test
    public void getGpuInfoSadPath() throws Exception {
        when(provider.gpuInfo()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/gpus")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}