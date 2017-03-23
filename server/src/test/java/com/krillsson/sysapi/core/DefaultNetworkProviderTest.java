package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.util.Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public void shouldCorrectData() throws Exception {
        NetworkIF mock = mock(NetworkIF.class);
        int i = 3;
        long rx = 100L;
        long tx = 200L;
        long startMillis = 1000L;
        long endMillis = 6000L;
        long resultRxbps = (((rx * i) - rx) * 8) / TimeUnit.MILLISECONDS.toSeconds(endMillis - startMillis);
        long resultTxbps = (((tx * i) - tx) * 8) / TimeUnit.MILLISECONDS.toSeconds(endMillis - startMillis);

        when(mock.getBytesRecv()).thenReturn(rx, rx * i);
        when(mock.getBytesSent()).thenReturn(tx, tx * i);
        when(utils.currentSystemTime()).thenReturn(startMillis, endMillis);

        when(mock.getName()).thenReturn("en1");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock});

        Optional<NetworkInterfaceData> en1 = provider.getNetworkInterfaceById("en1");
        assertTrue(en1.isPresent());
        NetworkInterfaceData networkInterfaceData = en1.get();
        NetworkInterfaceSpeed networkInterfaceSpeed = networkInterfaceData.getNetworkInterfaceSpeed();
        assertEquals(networkInterfaceSpeed.getRxbps(), resultRxbps);
        assertEquals(networkInterfaceSpeed.getTxbps(), resultTxbps);
    }
}