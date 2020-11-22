package com.krillsson.sysapi.core.metrics.rasbian;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RaspbianLinuxInfoProviderTest {

    RaspbianCpuSensors infoProvider;
    HardwareAbstractionLayer hal;
    OperatingSystem os;
    Utils utils;
    DefaultDriveMetrics diskProvider;
    DefaultNetworkMetrics networkProvider;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        utils = mock(Utils.class);
        diskProvider = mock(DefaultDriveMetrics.class);
        networkProvider = mock(DefaultNetworkMetrics.class);
        infoProvider = new TestableRaspbianLinuxInfoProvider(hal);
    }

    @Test
    public void testHappyPath() throws Exception {
        ((TestableRaspbianLinuxInfoProvider) infoProvider).setCommandOutput("volt=1.2938V");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(1.2938, voltage, 0.0);
    }

    @Test
    public void testSadPath() throws Exception {
        ((TestableRaspbianLinuxInfoProvider) infoProvider).setCommandOutput("volt=NaNV");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(0.0, voltage, 0.0);
    }

    @Test
    public void testInvalidOutput() throws Exception {
        ((TestableRaspbianLinuxInfoProvider) infoProvider).setCommandOutput("command not found: 127");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(0.0, voltage, 0.0);
    }

    private static class TestableRaspbianLinuxInfoProvider extends RaspbianCpuSensors {

        private String commandOutput;

        public TestableRaspbianLinuxInfoProvider(HardwareAbstractionLayer hal) {
            super(hal);
        }


        public void setCommandOutput(String commandOutput) {
            this.commandOutput = commandOutput;
        }

        @Override
        public String executeCommand() {
            return commandOutput;
        }
    }
}