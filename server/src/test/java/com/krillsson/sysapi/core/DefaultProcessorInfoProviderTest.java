package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class DefaultProcessorInfoProviderTest {
    DefaultProcessorInfoProvider infoProvider;
    HardwareAbstractionLayer hal;
    OperatingSystem os;
    Utils utils;
    private CentralProcessor centralProcessor;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        utils = mock(Utils.class);

        centralProcessor = mock(CentralProcessor.class);
        infoProvider = new DefaultProcessorInfoProvider(hal, os, utils);

    }


    @Test
    public void cpuLoadHappyPath() throws Exception {
        long[][] initialTicks = {{1402976, 0, 448399, 2780631, 0, 0, 0, 0}};
        long[][] secondTicks = {{1416739, 0, 452315, 2799166, 0, 0, 0, 0}};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getLogicalProcessorCount()).thenReturn(1);
        when(centralProcessor.getProcessorCpuLoadTicks()).thenReturn(initialTicks, secondTicks);
        when(utils.currentSystemTime()).thenReturn(1000L, 2000L);

        CpuLoad cpuLoad = infoProvider.cpuLoad();

        assertEquals(38.0, cpuLoad.getCoreLoads()[0].getUser(), 0.0);
        assertEquals(51.18, cpuLoad.getCoreLoads()[0].getIdle(), 0.0);
        assertEquals(10.81, cpuLoad.getCoreLoads()[0].getSys(), 0.0);
        assertNotNull(cpuLoad);
        verify(utils, times(1)).sleep(anyLong());
    }

    @Test
    public void cpuLoadOnlySleepsOnceIfInsideSamplingThreshold() throws Exception {
        long[][] initialTicks = {{1402976, 0, 448399, 2780631, 0, 0, 0, 0}};
        long[][] secondTicks = {{1416739, 0, 452315, 2799166, 0, 0, 0, 0}};
        long[][] thirdTicks = {{1466769, 0, 472315, 2899166, 0, 0, 0, 0}};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getLogicalProcessorCount()).thenReturn(1);
        when(centralProcessor.getProcessorCpuLoadTicks()).thenReturn(initialTicks, secondTicks, thirdTicks);
        when(utils.currentSystemTime()).thenReturn(1000L, 2000L, 3000L);

        CpuLoad cpuLoad = infoProvider.cpuLoad();
        CpuLoad secondCpuLoad = infoProvider.cpuLoad();


        assertFalse(cpuLoad.getCoreLoads()[0].getUser() == secondCpuLoad.getCoreLoads()[0].getUser());
        assertFalse(cpuLoad.getCoreLoads()[0].getIdle() == secondCpuLoad.getCoreLoads()[0].getIdle());
        assertFalse(cpuLoad.getCoreLoads()[0].getSys() == secondCpuLoad.getCoreLoads()[0].getSys());

        verify(utils, times(1)).sleep(anyLong());
    }
}