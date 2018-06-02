package com.krillsson.sysapi.core.metrics.defaultimpl;

import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultCpuSensorsTest {

    HardwareAbstractionLayer hal;
    Sensors sensors;

    DefaultCpuSensors cpuSensors;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        sensors = mock(Sensors.class);
        when(hal.getSensors()).thenReturn(sensors);
        when(sensors.getCpuTemperature()).thenReturn(30.0);
        when(sensors.getFanSpeeds()).thenReturn(new int[]{650/*RPM*/});
        when(sensors.getCpuVoltage()).thenReturn(1.35/*V*/);

        cpuSensors = new DefaultCpuSensors(hal);
    }

    @Test
    public void happyPath() {
        assertEquals(cpuSensors.cpuFanPercent(), 0, 0.0);
        assertEquals(cpuSensors.cpuFanRpm(), 650.0, 0.0);
        assertEquals(cpuSensors.cpuTemperatures().get(0), 30.0, 0.0);
        assertEquals(cpuSensors.cpuVoltage(), 1.35, 0.0);
    }

    @Test
    public void sadPath() {
        when(sensors.getCpuTemperature()).thenReturn(Double.NaN);
        when(sensors.getFanSpeeds()).thenReturn(new int[0]);
        when(sensors.getCpuVoltage()).thenReturn(0.0d/*V*/);

        assertEquals(cpuSensors.cpuFanPercent(), 0, 0.0);
        assertEquals(cpuSensors.cpuFanRpm(), 0.0, 0.0);
        assertEquals(cpuSensors.cpuTemperatures().get(0), 0.0, 0.0);
        assertEquals(cpuSensors.cpuVoltage(), 0.0, 0.0);
    }
}