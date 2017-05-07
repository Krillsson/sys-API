package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class DefaultInfoProviderTest {

    DefaultInfoProvider infoProvider;
    HardwareAbstractionLayer hal;
    OperatingSystem os;
    Utils utils;
    DefaultDiskProvider diskProvider;
    DefaultNetworkProvider networkProvider;
    private CentralProcessor centralProcessor;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        utils = mock(Utils.class);
        diskProvider = mock(DefaultDiskProvider.class);
        networkProvider = mock(DefaultNetworkProvider.class);
        centralProcessor = mock(CentralProcessor.class);
        infoProvider = new DefaultInfoProvider(hal, os, utils, networkProvider, diskProvider);

    }


    @Test
    public void cpuLoadHappyPath() throws Exception {
        long[] initialTicks = {1402976, 0, 448399, 2780631, 0, 0, 0};
        long[] secondTicks = {1416739, 0, 452315, 2799166, 0, 0, 0};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getSystemCpuLoadTicks()).thenReturn(initialTicks, secondTicks);
        when(utils.currentSystemTime()).thenReturn(1000L, 2000L);

        CpuLoad cpuLoad = infoProvider.cpuLoad();

        assertEquals(38.0, cpuLoad.getUser(), 0.0);
        assertEquals(51.18, cpuLoad.getIdle(), 0.0);
        assertEquals(10.81, cpuLoad.getSys(), 0.0);
        assertNotNull(cpuLoad);
        verify(utils, times(1)).sleep(anyLong());
    }

    @Test
    public void cpuLoadOnlySleepsOnceIfInsideSamplingThreshold() throws Exception {
        long[] initialTicks = {1402976, 0, 448399, 2780631, 0, 0, 0};
        long[] secondTicks = {1416739, 0, 452315, 2799166, 0, 0, 0};
        long[] thirdTicks = {1466769, 0, 472315, 2899166, 0, 0, 0};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getSystemCpuLoadTicks()).thenReturn(initialTicks, secondTicks, thirdTicks);
        when(utils.currentSystemTime()).thenReturn(1000L, 2000L, 3000L);

        CpuLoad cpuLoad = infoProvider.cpuLoad();
        CpuLoad secondCpuLoad = infoProvider.cpuLoad();


        assertFalse(cpuLoad.getUser() == secondCpuLoad.getUser());
        assertFalse(cpuLoad.getIdle() == secondCpuLoad.getIdle());
        assertFalse(cpuLoad.getSys() == secondCpuLoad.getSys());

        verify(utils, times(1)).sleep(anyLong());
    }
}