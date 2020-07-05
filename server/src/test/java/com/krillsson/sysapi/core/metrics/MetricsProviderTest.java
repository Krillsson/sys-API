package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.config.MetricsConfiguration;
import com.krillsson.sysapi.config.SysAPIConfiguration;
import com.krillsson.sysapi.core.metrics.cache.Cache;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics;
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetrics;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import com.krillsson.sysapi.util.Ticker;
import org.junit.Before;
import org.junit.Test;
import oshi.PlatformEnum;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetricsProviderTest {
    private HardwareAbstractionLayer hal;
    private oshi.software.os.OperatingSystem os;
    private SysAPIConfiguration config;
    private SpeedMeasurementManager measurementManager;
    private Ticker ticker;
    private MetricsConfiguration metricsConfig;
    private CacheConfiguration cacheConfig;
    private CentralProcessor centralProcessor;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        os = mock(OperatingSystem.class);
        config = mock(SysAPIConfiguration.class);
        metricsConfig = mock(MetricsConfiguration.class);
        cacheConfig = mock(CacheConfiguration.class);
        when(cacheConfig.getDuration()).thenReturn(5L);
        when(cacheConfig.getUnit()).thenReturn(TimeUnit.MILLISECONDS);
        //centralProcessor = mock(CentralProcessor.class);

        when(metricsConfig.getCache()).thenReturn(cacheConfig);
        when(config.metrics()).thenReturn(metricsConfig);
        ticker = mock(Ticker.class);

        measurementManager = mock(SpeedMeasurementManager.class);
        when(hal.getNetworkIFs()).thenReturn(Collections.emptyList());
        when(hal.getDiskStores()).thenReturn(Collections.emptyList());
        //when(hal.getProcessor()).thenReturn(centralProcessor);
    }

    @Test
    public void providingDefaultIfPlatformIsUnknown() throws Exception {
        MetricsFactory factory = new MetricsFactory(hal, os, PlatformEnum.UNKNOWN, measurementManager, ticker);
        factory.setCache(false);

        Metrics provider = factory.get(config);
        assertNotNull(provider);
        assertTrue(provider instanceof DefaultMetrics);
    }

    @Test
    public void providerDetectsRaspbian() throws Exception {
        when(os.getFamily()).thenReturn("Raspbian GNU/Linux");
        MetricsFactory factory = new MetricsFactory(hal, os, PlatformEnum.LINUX, measurementManager, ticker);
        factory.setCache(false);

        Metrics provider = factory.get(config);
        assertNotNull(provider);
        assertTrue(provider instanceof RaspbianMetrics);
    }

    @Test
    public void providerDetectsLinux() throws Exception {
        when(os.getFamily()).thenReturn("Debian GNU/Linux");
        MetricsFactory factory = new MetricsFactory(hal, os, PlatformEnum.LINUX, measurementManager, ticker);
        factory.setCache(false);

        Metrics provider = factory.get(config);
        assertNotNull(provider);
        assertFalse(provider instanceof RaspbianMetrics);
        assertTrue(provider instanceof DefaultMetrics);
    }

    @Test
    public void cachesByDefault() throws Exception {
        MetricsFactory factory = new MetricsFactory(hal, os, PlatformEnum.UNKNOWN, measurementManager, ticker);

        Metrics provider = factory.get(config);
        assertNotNull(provider);
        assertTrue(provider instanceof Cache);
    }
}