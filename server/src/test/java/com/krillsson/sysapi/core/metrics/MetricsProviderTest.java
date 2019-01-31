package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.config.MetricsConfiguration;
import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.util.Ticker;
import com.krillsson.sysapi.core.metrics.cache.Cache;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetricsFactory;
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetricsFactory;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import org.junit.Before;
import org.junit.Test;
import oshi.PlatformEnum;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetricsProviderTest {
    private HardwareAbstractionLayer hal;
    private oshi.software.os.OperatingSystem os;
    private SystemApiConfiguration config;
    private SpeedMeasurementManager measurementManager;
    private Ticker ticker;
    private MetricsConfiguration metricsConfig;
    private CacheConfiguration cacheConfig;
    private CentralProcessor centralProcessor;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        config = mock(SystemApiConfiguration.class);
        metricsConfig = mock(MetricsConfiguration.class);
        cacheConfig = mock(CacheConfiguration.class);
        when(cacheConfig.getDuration()).thenReturn(5L);
        when(cacheConfig.getUnit()).thenReturn(TimeUnit.MILLISECONDS);
        //centralProcessor = mock(CentralProcessor.class);

        when(metricsConfig.getCache()).thenReturn(cacheConfig);
        when(config.metrics()).thenReturn(metricsConfig);
        ticker = mock(Ticker.class);

        measurementManager = mock(SpeedMeasurementManager.class);
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[0]);
        when(hal.getDiskStores()).thenReturn(new HWDiskStore[0]);
        //when(hal.getProcessor()).thenReturn(centralProcessor);
    }

    @Test
    public void providingDefaultIfPlatformIsUnknown() throws Exception {
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.UNKNOWN, config, measurementManager, ticker);
        factory.setCache(false);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertTrue(provider instanceof DefaultMetricsFactory);
    }

    @Test
    public void providerDetectsRaspbian() throws Exception {
        when(os.getFamily()).thenReturn("Raspbian GNU/Linux");
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.LINUX, config, measurementManager, ticker);
        factory.setCache(false);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertTrue(provider instanceof RaspbianMetricsFactory);
    }

    @Test
    public void providerDetectsLinux() throws Exception {
        when(os.getFamily()).thenReturn("Debian GNU/Linux");
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.LINUX, config, measurementManager, ticker);
        factory.setCache(false);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertFalse(provider instanceof RaspbianMetricsFactory);
        assertTrue(provider instanceof DefaultMetricsFactory);
    }

    @Test
    public void cachesByDefault() throws Exception {
        MetricsProvider factory = new MetricsProvider(hal, os, PlatformEnum.UNKNOWN, config, measurementManager, ticker);

        MetricsFactory provider = factory.create();
        assertNotNull(provider);
        assertTrue(provider instanceof Cache);
    }
}