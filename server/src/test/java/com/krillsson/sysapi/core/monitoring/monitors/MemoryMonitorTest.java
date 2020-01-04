package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryMonitorTest {
    SystemLoad systemLoad;
    MemoryLoad memoryLoad;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        memoryLoad = mock(MemoryLoad.class);
        when(systemLoad.getMemory()).thenReturn(memoryLoad);
    }

    @Test
    public void monitorValuesCorrectly() {
        MemoryMonitor memoryMonitor = new MemoryMonitor("mem", Duration.ofSeconds(0), 1024);

        when(memoryLoad.getAvailable()).thenReturn(512L);
        assertTrue(memoryMonitor.isOutsideThreshold(memoryMonitor.value(systemLoad)));

        when(memoryLoad.getAvailable()).thenReturn(2048L);
        assertFalse(memoryMonitor.isOutsideThreshold(memoryMonitor.value(systemLoad)));
    }
}