package com.krillsson.sysapi.core.metrics.rasbian;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDiskProvider;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkProvider;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RaspbianLinuxInfoProviderTest {

    RaspbianLinuxInfoProvider infoProvider;
    HardwareAbstractionLayer hal;
    OperatingSystem os;
    Utils utils;
    DefaultDiskProvider diskProvider;
    DefaultNetworkProvider networkProvider;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        utils = mock(Utils.class);
        diskProvider = mock(DefaultDiskProvider.class);
        networkProvider = mock(DefaultNetworkProvider.class);
        infoProvider = new TestableRaspbianLinuxInfoProvider(hal, os, utils, networkProvider, diskProvider);
    }

    @Test
    public void testHappyPath() throws Exception {
        ((TestableRaspbianLinuxInfoProvider)infoProvider).setCommandOutput("volt=1.2938V");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(1.2938, voltage, 0.0);
    }

    @Test
    public void testSadPath() throws Exception {
        ((TestableRaspbianLinuxInfoProvider)infoProvider).setCommandOutput("volt=NaNV");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(0.0, voltage, 0.0);
    }

    @Test
    public void testInvalidOutput() throws Exception {
        ((TestableRaspbianLinuxInfoProvider)infoProvider).setCommandOutput("command not found: 127");

        double voltage = infoProvider.cpuVoltage();
        assertEquals(0.0, voltage, 0.0);
    }

    private static class TestableRaspbianLinuxInfoProvider extends RaspbianLinuxInfoProvider{

        private String commandOutput;

        TestableRaspbianLinuxInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils, DefaultNetworkProvider defaultNetworkProvider, DefaultDiskProvider defaultDiskProvider) {
            super(hal, operatingSystem, utils, defaultNetworkProvider, defaultDiskProvider);
        }

        public void setCommandOutput(String commandOutput) {
            this.commandOutput = commandOutput;
        }

        @Override
        String executeCommand() {
            return commandOutput;
        }
    }
}