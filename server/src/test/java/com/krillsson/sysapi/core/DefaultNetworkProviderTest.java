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
    private Utils utils;

    @Before
    public void setUp() throws Exception {
        hal = mock(HardwareAbstractionLayer.class);
        utils = new TestableUtils();
        provider = new DefaultNetworkProvider(hal, utils);
    }

    @Test
    public void testGetNames() throws Exception {
        NetworkIF mock = mock(NetworkIF.class);
        NetworkIF secondMock = mock(NetworkIF.class);

        when(mock.getName()).thenReturn("en1");
        when(secondMock.getName()).thenReturn("en2");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock, secondMock});

        String[] allNetworkInterfaces = provider.getNetworkInterfaceNames();

        assertEquals(2, allNetworkInterfaces.length);
        assertEquals("en1", allNetworkInterfaces[0]);
        assertEquals("en2", allNetworkInterfaces[1]);
    }

    @Test
    public void shouldNotSleepIfThereAreNoInterfacesFound() throws Exception {
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[0]);

        assertFalse(provider.getNetworkInterfaceById("en1").isPresent());
        assertArrayEquals(new NetworkInterfaceData[0], provider.getAllNetworkInterfaces());
        assertFalse(provider.getSpeed("en1").isPresent());
        assertEquals(0, ((TestableUtils) utils).sleepCalled, 0);
    }

    @Test
    public void shouldOnlySleepOnceForMultipleMeasurements() throws Exception {
        long i = 3;
        long rx = 12345;
        long tx = 54321;
        NetworkIF mock = mock(NetworkIF.class);
        NetworkIF secondMock = mock(NetworkIF.class);

        when(mock.getBytesRecv()).thenReturn(rx, rx * i);
        when(secondMock.getBytesRecv()).thenReturn(rx, rx * i);
        when(mock.getBytesSent()).thenReturn(tx, tx * i);
        when(secondMock.getBytesSent()).thenReturn(tx, tx * i);

        when(mock.getName()).thenReturn("en1");
        when(secondMock.getName()).thenReturn("en2");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock, secondMock});

        NetworkInterfaceData[] allNetworkInterfaces = provider.getAllNetworkInterfaces();

        assertEquals(2, allNetworkInterfaces.length);
        assertEquals(1, ((TestableUtils) utils).sleepCalled, 0);
    }

    @Test
    public void shouldSleepIfTheStoredMeasurementIsTooOld() throws Exception {
        long i = 3;
        long rx = 12345;
        long tx = 54321;
        NetworkIF mock = mock(NetworkIF.class);

        when(mock.getBytesRecv()).thenReturn(rx, rx * i);
        when(mock.getBytesSent()).thenReturn(tx, tx * i);

        when(mock.getName()).thenReturn("en1");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock});

        Optional<NetworkInterfaceData> en1 = provider.getNetworkInterfaceById("en1");
        ((TestableUtils) utils).systemTime += 20000;
        Optional<NetworkInterfaceData> en1Second = provider.getNetworkInterfaceById("en1");
        assertEquals(2, ((TestableUtils) utils).sleepCalled, 0);
        assertTrue(en1.isPresent());
        assertTrue(en1Second.isPresent());
    }

    @Test
    public void shouldCorrectData() throws Exception {
        NetworkIF mock = mock(NetworkIF.class);
        long i = 3;
        long rx = 12345;
        long tx = 54321;
        double startMillis = 1000.0;
        double endMillis = 3000.0;
        long resultRxbps = (long) ((((rx * i) - rx) * 8.0) / ((endMillis - startMillis) / 1000.0));
        long resultTxbps = (long) ((((tx * i) - tx) * 8.0) / ((endMillis - startMillis) / 1000.0));

        when(mock.getBytesRecv()).thenReturn(rx, rx * i);
        when(mock.getBytesSent()).thenReturn(tx, tx * i);

        when(mock.getName()).thenReturn("en1");
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{mock});

        Optional<NetworkInterfaceData> en1 = provider.getNetworkInterfaceById("en1");
        assertTrue(en1.isPresent());
        NetworkInterfaceData networkInterfaceData = en1.get();
        NetworkInterfaceSpeed networkInterfaceSpeed = networkInterfaceData.getNetworkInterfaceSpeed();
        assertEquals(resultRxbps, networkInterfaceSpeed.getRxbps());
        assertEquals(resultTxbps, networkInterfaceSpeed.getTxbps());
        assertEquals(utils.currentSystemTime(), endMillis, 0);
        //for checking r, x and then for checking if it's outdated
        assertEquals(3, ((TestableUtils) utils).getTimeCalled, 0);
        assertEquals(1, ((TestableUtils) utils).sleepCalled, 0);
    }

    private class TestableUtils extends Utils {
        long systemTime = 1000;
        int sleepCalled = 0;
        int getTimeCalled = 0;

        @Override
        public long currentSystemTime() {
            getTimeCalled++;
            return systemTime;
        }

        @Override
        public void sleep(long ms) {
            systemTime += ms;
            sleepCalled++;
        }
    }
}