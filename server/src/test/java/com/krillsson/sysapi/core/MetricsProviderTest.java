package com.krillsson.sysapi.core;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import com.krillsson.sysapi.core.metrics.MetricsProvider;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetricsFactory;
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianLinuxInfoProviderTest;
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetricsFactory;
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

public class MetricsProviderTest {

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
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.UNKNOWN, config, measurementManager);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertTrue(provider instanceof DefaultMetricsFactory);
    }

    @Test
    public void providerDetectsRaspbian() throws Exception {
        when(os.getFamily()).thenReturn("Raspbian GNU/Linux");
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.LINUX, config, measurementManager);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertTrue(provider instanceof RaspbianMetricsFactory);
    }

    @Test
    public void providerDetectsLinux() throws Exception {
        when(os.getFamily()).thenReturn("Debian GNU/Linux");
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.LINUX, config, measurementManager);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertFalse(provider instanceof RaspbianMetricsFactory);
        assertTrue(provider instanceof DefaultMetricsFactory);
    }

}