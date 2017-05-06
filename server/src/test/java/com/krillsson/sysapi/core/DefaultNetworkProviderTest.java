package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultNetworkProviderTest {

    DefaultNetworkProvider provider;
    private HardwareAbstractionLayer hal;
    private SpeedMeasurementManager speedMeasurementManager;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        speedMeasurementManager = mock(SpeedMeasurementManager.class);
    }

    @Test
    public void testGetNames() throws Exception {

        NetworkIF mock = mock(NetworkIF.class);
        NetworkIF secondMock = mock(NetworkIF.class);

        when(mock.getName()).thenReturn("en1");
        when(secondMock.getName()).thenReturn("en2");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock, secondMock});

        provider = new DefaultNetworkProvider(hal, speedMeasurementManager);

        String[] allNetworkInterfaces = provider.getNetworkInterfaceNames();

        assertEquals(2, allNetworkInterfaces.length);
        assertEquals("en1", allNetworkInterfaces[0]);
        assertEquals("en2", allNetworkInterfaces[1]);
    }

}