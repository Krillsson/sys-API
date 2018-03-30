package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class DefaultProcessesMetricsTest {
    HardwareAbstractionLayer hal;
    GlobalMemory memory;
    OperatingSystem os;
    DefaultProcessesMetrics provider;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        memory = mock(GlobalMemory.class);
        when(hal.getMemory()).thenReturn(memory);
        os = mock(OperatingSystem.class);

        provider = new DefaultProcessesMetrics(os, hal);
    }

    @Test
    public void shouldHandleNoProcessesPresent() {
        when(os.getProcesses(any(), any(OperatingSystem.ProcessSort.class))).thenReturn(new OSProcess[0]);
        ProcessesInfo processesInfo = provider.processesInfo(OperatingSystem.ProcessSort.CPU, 0);

        assertTrue(processesInfo.getProcesses().isEmpty());
    }

    @Test
    public void shouldProperlyAssignPercentageUsedOfMemory() {
        //100d * process.getResidentSetSize() / memory.getTotal()
        double usage = 100d * 1000L / 4000L;
        assertEquals(usage, 25L, 0);

        OSProcess process = mock(OSProcess.class);
        when(process.getResidentSetSize()).thenReturn(1000L);
        when(os.getProcesses(any(), any(OperatingSystem.ProcessSort.class))).thenReturn(new OSProcess[]{process});
        //when(memory.getAvailable()).thenReturn(3000L);
        when(memory.getTotal()).thenReturn(4000L);

        ProcessesInfo processes = provider.processesInfo(OperatingSystem.ProcessSort.CPU, 0);

        assertEquals(processes.getProcesses().get(0).getMemoryPercent(), usage, 0);
    }

    @Test
    public void shouldProperlyAssignPercentageUsedOfCpu() {
        
    }
}