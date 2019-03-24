package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.CpuMetrics;
import com.krillsson.sysapi.util.OffsetDateTimeConverter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CpuResourceTest {
    private static final CpuMetrics provider = mock(CpuMetrics.class);
    private static final MetricsHistoryManager historyManager = mock(MetricsHistoryManager.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new CpuResource(provider, historyManager))
            .addProvider(OffsetDateTimeConverter.class)
            .build();

    @Test
    public void getCpuHappyPath() throws Exception {
        oshi.hardware.CentralProcessor centralProcessor = mock(oshi.hardware.CentralProcessor.class);
        when(provider.cpuInfo()).thenReturn(new CpuInfo(centralProcessor));

        final com.krillsson.sysapi.dto.cpu.CpuInfo response = RESOURCES.getJerseyTest().target("/cpu")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.cpu.CpuInfo.class);
        assertNotNull(response);
        //assertEquals(response.getCpuLoad().getCpuLoadCountingTicks(), 100, 0);
        //, new CpuLoad(100, 0, new CoreLoad[]{}, cpuHealth, processCount, threadCount), new CpuHealth(new double[0], 120, 1000, 10
    }

    @Test
    public void getCpuSadPath() throws Exception {
        when(provider.cpuInfo()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/cpu")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}
