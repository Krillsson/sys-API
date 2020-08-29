package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CpuMonitorTest {

    SystemLoad systemLoad;
    CpuLoad cpuLoad;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        cpuLoad = mock(CpuLoad.class);
        when(systemLoad.getCpuLoad()).thenReturn(cpuLoad);
    }

    @Test
    public void monitorValuesCorrectly() {
        when(cpuLoad.getCpuLoadOsMxBean()).thenReturn(0.30);
        CpuMonitor cpuMonitor = new CpuMonitor(UUID.randomUUID(), new MonitorConfig(null, 0.20, Duration.ZERO));
        assertTrue(cpuMonitor.check(systemLoad));
        when(cpuLoad.getCpuLoadOsMxBean()).thenReturn(0.10);
        assertFalse(cpuMonitor.check(systemLoad));
    }
}