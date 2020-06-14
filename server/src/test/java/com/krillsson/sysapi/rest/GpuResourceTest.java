package com.krillsson.sysapi.rest;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.GpuMetrics;
import com.krillsson.sysapi.util.OffsetDateTimeConverter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class GpuResourceTest {
    private static final GpuMetrics provider = mock(GpuMetrics.class);
    private static final MetricsHistoryManager historyManager = mock(MetricsHistoryManager.class);
    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new GpuResource(provider, historyManager))
            .addProvider(OffsetDateTimeConverter.class)
            .build();
    Gpu gpu;
    GpuHealth health;
    GpuLoad load;

    @Before
    public void setUp() throws Exception {
        gpu = new Gpu("Nvidia", "GTX 970", 1000.0, 400.0);
        health = new GpuHealth(0.0, 0.0, 40);
        load = new GpuLoad("GTX 970", 0.5, 0.5, health);
    }

    @Test
    public void getGpuInfoHappyPath() throws Exception {
        when(provider.gpus()).thenReturn(Arrays.asList(gpu));
        final List<com.krillsson.sysapi.dto.gpu.Gpu> response = RESOURCES.getJerseyTest().target("/gpus")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<com.krillsson.sysapi.dto.gpu.Gpu>>() {
                });
        assertNotNull(response);
        assertEquals("Nvidia", response.get(0).getVendor());
    }

    @Test
    public void getGpuInfoSadPath() throws Exception {
        when(provider.gpus()).thenThrow(new RuntimeException("What"));
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