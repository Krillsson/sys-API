package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;
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
        CpuMonitor cpuMonitor = new CpuMonitor("cpu0", Duration.ofSeconds(0), 0.20);
        assertTrue(cpuMonitor.isOutsideThreshold(cpuMonitor.value(systemLoad)));
        when(cpuLoad.getCpuLoadOsMxBean()).thenReturn(0.10);
        assertFalse(cpuMonitor.isOutsideThreshold(cpuMonitor.value(systemLoad)));
    }
}