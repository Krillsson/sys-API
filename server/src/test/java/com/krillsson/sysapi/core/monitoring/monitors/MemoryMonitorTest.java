package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.drives.DriveValues;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.GlobalMemory;

import java.time.Duration;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryMonitorTest {
    SystemLoad systemLoad;
    GlobalMemory globalMemory;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        globalMemory = mock(GlobalMemory.class);
        when(systemLoad.getMemory()).thenReturn(globalMemory);
    }

    @Test
    public void monitorValuesCorrectly() {
        MemoryMonitor memoryMonitor = new MemoryMonitor("mem", Duration.ofSeconds(0), 1024);

        when(globalMemory.getAvailable()).thenReturn(512L);
        assertTrue(memoryMonitor.isOutsideThreshold(memoryMonitor.value(systemLoad)));

        when(globalMemory.getAvailable()).thenReturn(2048L);
        assertFalse(memoryMonitor.isOutsideThreshold(memoryMonitor.value(systemLoad)));
    }
}