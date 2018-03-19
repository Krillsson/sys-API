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
}