package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkProvider;
import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultNetworkProviderTest {

    DefaultNetworkProvider provider;
    HardwareAbstractionLayer hal;
    SpeedMeasurementManager speedMeasurementManager;

    NetworkIF nic1;
    NetworkIF nic2;


    @Before
    public void setUp() {
        hal = mock(HardwareAbstractionLayer.class);
        speedMeasurementManager = mock(SpeedMeasurementManager.class);
        provider = new DefaultNetworkProvider(hal, speedMeasurementManager);

        nic1 = mock(NetworkIF.class);
        when(nic1.getName()).thenReturn("en0");
        nic2 = mock(NetworkIF.class);
        when(nic2.getName()).thenReturn("en1");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{nic1, nic2});
    }

    @Test
    public void networkInterfacesHappyPath() {
        List<NetworkInterface> networkInterfaces = provider.networkInterfaces();

        assertThat(networkInterfaces.size(), is(2));
    }


}