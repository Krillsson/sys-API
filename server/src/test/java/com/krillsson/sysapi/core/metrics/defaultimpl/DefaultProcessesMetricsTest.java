package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.processes.ProcessSort;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.util.Ticker;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Answers.RETURNS_MOCKS;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultProcessesMetricsTest {
    HardwareAbstractionLayer hal;
    GlobalMemory memory;
    VirtualMemory virtualMemory;
    OperatingSystem os;
    DefaultProcessesMetrics provider;
    Ticker ticker;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        memory = mock(GlobalMemory.class);
        virtualMemory = mock(VirtualMemory.class);

        when(hal.getMemory()).thenReturn(memory);
        when(memory.getVirtualMemory()).thenReturn(virtualMemory);
        os = mock(OperatingSystem.class);
        ticker = mock(Ticker.class);

        provider = new DefaultProcessesMetrics(os, hal, ticker);
    }

    @Test
    public void shouldHandleNoProcessesPresent() {
        when(os.getProcesses(anyInt(), any(OperatingSystem.ProcessSort.class))).thenReturn(Collections.emptyList());
        ProcessesInfo processesInfo = provider.processesInfo(ProcessSort.CPU, 0);

        assertTrue(processesInfo.getProcesses().isEmpty());
    }

    @Test
    public void shouldProperlyAssignPercentageUsedOfMemory() {
        //100d * process.getResidentSetSize() / memory.getTotal()
        double usage = 100d * 1000L / 4000L;
        assertEquals(usage, 25L, 0);

        OSProcess process = mock(OSProcess.class, RETURNS_MOCKS);
        when(process.getResidentSetSize()).thenReturn(1000L);
        when(os.getProcesses(anyInt(), any(OperatingSystem.ProcessSort.class))).thenReturn(Collections.singletonList(process));
        //when(memory.getAvailable()).thenReturn(3000L);
        when(memory.getTotal()).thenReturn(4000L);

        ProcessesInfo processes = provider.processesInfo(ProcessSort.CPU, 0);

        assertEquals(processes.getProcesses().get(0).getMemoryPercent(), usage, 0);
    }

    @Test
    public void shouldProperlyAssignPercentageUsedOfCpu() {
        //100d * (process.getKernelTime() + process.getUserTime()) / process.getUpTime()
        double usage = 100d * 20 / 100;
        assertEquals(usage, 20, 0);

        OSProcess process = mock(OSProcess.class, RETURNS_MOCKS);
        when(process.getKernelTime()).thenReturn(16L);
        when(process.getUserTime()).thenReturn(4L);
        when(process.getUpTime()).thenReturn(100L);
        when(os.getProcesses(anyInt(), any(OperatingSystem.ProcessSort.class))).thenReturn(Collections.singletonList(process));

        ProcessesInfo processes = provider.processesInfo(ProcessSort.CPU, 0);

        assertEquals(processes.getProcesses().get(0).getCpuPercent(), usage, 0);
    }
}