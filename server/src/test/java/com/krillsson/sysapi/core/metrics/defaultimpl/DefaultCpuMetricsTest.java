package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.util.Ticker;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class DefaultCpuMetricsTest {
    DefaultCpuMetrics infoProvider;
    HardwareAbstractionLayer hal;
    OperatingSystem os;
    Utils utils;
    Sensors sensors;
    Ticker ticker;
    DefaultCpuSensors cpuSensors;

    CentralProcessor centralProcessor;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        utils = mock(Utils.class);
        sensors = mock(Sensors.class);
        ticker = mock(Ticker.class);

        centralProcessor = mock(CentralProcessor.class);
        //when(centralProcessor.getSystemCpuLoadTicks()).thenReturn(new long[]{1402976, 0, 448399, 2780631, 0, 0, 0, 0});
        when(centralProcessor.getSystemLoadAverage(anyInt())).thenReturn(new double[]{1.0});

        when(hal.getSensors()).thenReturn(sensors);
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(sensors.getCpuTemperature()).thenReturn(30.0);
        when(sensors.getCpuVoltage()).thenReturn(1.35);
        when(sensors.getFanSpeeds()).thenReturn(new int[]{1200});
        cpuSensors = new DefaultCpuSensors(hal);
        infoProvider = new DefaultCpuMetrics(hal, os, cpuSensors, utils, ticker);
    }


    @Test
    public void cpuLoadHappyPath() throws Exception {
        long[][] initialTicks = {{1402976, 0, 448399, 2780631, 0, 0, 0, 0}};
        long[][] secondTicks = {{1416739, 0, 452315, 2799166, 0, 0, 0, 0}};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getLogicalProcessorCount()).thenReturn(1);
        when(centralProcessor.getProcessorCpuLoadTicks()).thenReturn(initialTicks, secondTicks);

        infoProvider.onTick();
        CpuLoad cpuLoad = infoProvider.cpuLoad();

        assertEquals(38.0, cpuLoad.getCoreLoads().get(0).getUser(), 0.0);
        assertEquals(51.18, cpuLoad.getCoreLoads().get(0).getIdle(), 0.0);
        assertEquals(10.81, cpuLoad.getCoreLoads().get(0).getSys(), 0.0);
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

        infoProvider.onTick();
        CpuLoad cpuLoad = infoProvider.cpuLoad();
        infoProvider.onTick();
        CpuLoad secondCpuLoad = infoProvider.cpuLoad();


        assertFalse(cpuLoad.getCoreLoads().get(0).getUser() == secondCpuLoad.getCoreLoads().get(0).getUser());
        assertFalse(cpuLoad.getCoreLoads().get(0).getIdle() == secondCpuLoad.getCoreLoads().get(0).getIdle());
        assertFalse(cpuLoad.getCoreLoads().get(0).getSys() == secondCpuLoad.getCoreLoads().get(0).getSys());

        verify(utils, times(1)).sleep(anyLong());
    }

    @Test
    public void cpuHealthHappyPath() {
        long[][] initialTicks = {{1402976, 0, 448399, 2780631, 0, 0, 0, 0}};
        long[][] secondTicks = {{1416739, 0, 452315, 2799166, 0, 0, 0, 0}};
        when(hal.getProcessor()).thenReturn(centralProcessor);
        when(centralProcessor.getLogicalProcessorCount()).thenReturn(1);
        when(centralProcessor.getProcessorCpuLoadTicks()).thenReturn(initialTicks, secondTicks);

        infoProvider.onTick();
        CpuHealth cpuHealth = infoProvider.cpuLoad().getCpuHealth();
        assertEquals(cpuHealth.getFanPercent(), 0, 0);
        assertEquals(cpuHealth.getFanRpm(), 1200, 0);
        assertEquals(cpuHealth.getTemperatures().get(0), 30.0, 0);
        assertEquals(cpuHealth.getVoltage(), 1.35, 0);
    }
}