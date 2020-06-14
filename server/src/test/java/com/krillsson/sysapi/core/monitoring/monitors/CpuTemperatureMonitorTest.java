package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CpuTemperatureMonitorTest {
    SystemLoad systemLoad;
    CpuLoad cpuLoad;
    CpuHealth cpuHealth;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        cpuLoad = mock(CpuLoad.class);
        cpuHealth = mock(CpuHealth.class);
        when(systemLoad.getCpuLoad()).thenReturn(cpuLoad);
        when(cpuLoad.getCpuHealth()).thenReturn(cpuHealth);
    }

    @Test
    public void monitorValuesCorrectly() {
        when(cpuHealth.getTemperatures()).thenReturn(Arrays.asList(100d));
        CpuTemperatureMonitor cpuMonitor = new CpuTemperatureMonitor(UUID.randomUUID(), new MonitorConfig(null, 90, Duration.ZERO));
        assertTrue(cpuMonitor.check(systemLoad));
        when(cpuHealth.getTemperatures()).thenReturn(Collections.emptyList());
        assertFalse(cpuMonitor.check(systemLoad));
    }
}