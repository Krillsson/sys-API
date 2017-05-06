package com.krillsson.sysapi.core;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.windows.WindowsInfoProvider;
import org.junit.Before;
import org.junit.Test;
import oshi.PlatformEnum;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InfoProviderFactoryTest {

    private HardwareAbstractionLayer hal;
    private oshi.software.os.OperatingSystem os;
    private SystemApiConfiguration config;
    private SpeedMeasurementManager measurementManager;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        config = mock(SystemApiConfiguration.class);
        measurementManager = mock(SpeedMeasurementManager.class);
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[0]);
        when(hal.getDiskStores()).thenReturn(new HWDiskStore[0]);

    }

    @Test
    public void providingDefaultIfPlatformIsUnknown() throws Exception {
        InfoProviderFactory factory = new InfoProviderFactory(hal, os, PlatformEnum.UNKNOWN, config, measurementManager);

        InfoProvider provider = factory.provide();
        assertNotNull(provider);
        assertTrue(provider instanceof DefaultInfoProvider);
    }
}