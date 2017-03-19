package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultNetworkProviderTest {

    DefaultNetworkProvider provider;
    private HardwareAbstractionLayer hal;
    private Utils utils;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        utils = mock(Utils.class);
        provider = new DefaultNetworkProvider(hal, utils);
    }

    @Test
    public void shouldReturnEmptyData() throws Exception {
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[0]);

        assertFalse(provider.getNetworkInterfaceById("en1").isPresent());
        assertArrayEquals(new NetworkInterfaceData[0], provider.getAllNetworkInterfaces());
        assertFalse(provider.getSpeed("en1").isPresent());
    }

    @Test
    @Ignore
    public void shouldCorrectData() throws Exception {
        NetworkIF mock = mock(NetworkIF.class);
        when(utils.currentSystemTime()).thenReturn(0L);
        when(utils.currentSystemTime()).thenReturn(1000L);

        when(mock.getName()).thenReturn("en1");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock});

        assertTrue(provider.getNetworkInterfaceById("en1").isPresent());
        assertArrayEquals(new NetworkInterfaceData[0], provider.getAllNetworkInterfaces());
        assertTrue(provider.getSpeed("en1").isPresent());
    }
}