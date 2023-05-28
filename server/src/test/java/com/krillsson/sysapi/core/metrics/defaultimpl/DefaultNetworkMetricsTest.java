package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import com.krillsson.sysapi.periodictasks.TaskExecutorJob;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultNetworkMetricsTest {

    DefaultNetworkMetrics provider;
    HardwareAbstractionLayer hal;
    ConnectivityCheckManager connectivityCheckManager;
    TaskExecutorJob taskManager;
    SpeedMeasurementManager speedMeasurementManager;

    NetworkIF nic1;
    NetworkIF nic2;
    private java.net.NetworkInterface networkInterface1;


    @Before
    public void setUp() throws SocketException {
        hal = mock(HardwareAbstractionLayer.class);
        connectivityCheckManager = mock(ConnectivityCheckManager.class);
        taskManager = mock(TaskExecutorJob.class);
        speedMeasurementManager = mock(SpeedMeasurementManager.class);
        provider = new DefaultNetworkMetrics(
                hal,
                speedMeasurementManager,
                connectivityCheckManager
        );

        nic1 = mock(NetworkIF.class);
        networkInterface1 = mock(java.net.NetworkInterface.class);
        when(networkInterface1.isLoopback()).thenReturn(false);
        when(nic1.getName()).thenReturn("en0");
        when(nic1.getDisplayName()).thenReturn("en0");
        when(nic1.getMacaddr()).thenReturn("00:1B:63:84:45:E6");
        when(nic1.getMTU()).thenReturn(1500L);
        when(nic1.getIPv4addr()).thenReturn(new String[]{"192.168.1.2"});
        when(nic1.getIPv6addr()).thenReturn(new String[]{"2001:0db8:85a3:0000:0000:8a2e:0370:7334"});
        when(nic1.getSpeed()).thenReturn(1000L);
        when(nic1.getBytesRecv()).thenReturn(123L);
        when(nic1.getBytesSent()).thenReturn(456L);
        when(nic1.getPacketsRecv()).thenReturn(10L);
        when(nic1.getPacketsSent()).thenReturn(20L);
        when(nic1.getInErrors()).thenReturn(30L);
        when(nic1.getOutErrors()).thenReturn(40L);
        when(nic1.queryNetworkInterface()).thenReturn(networkInterface1);
        when(speedMeasurementManager.getCurrentSpeedForName("en1")).thenReturn(Optional.of(new SpeedMeasurementManager.CurrentSpeed(
                125L,
                322L
        )));

        nic2 = mock(NetworkIF.class);
        java.net.NetworkInterface networkInterface2 = mock(java.net.NetworkInterface.class);
        when(networkInterface2.isLoopback()).thenReturn(false);
        when(nic2.getName()).thenReturn("en1");
        when(nic2.getDisplayName()).thenReturn("en1");
        when(nic2.getMacaddr()).thenReturn("00:1B:63:84:45:E8");
        when(nic2.getMTU()).thenReturn(1500L);
        when(nic2.getIPv4addr()).thenReturn(new String[]{"192.168.1.3"});
        when(nic2.getIPv6addr()).thenReturn(new String[]{"2001:0db8:85a3:0000:0000:8a2e:0370:7335"});
        when(nic2.getSpeed()).thenReturn(1001L);
        when(nic2.getBytesRecv()).thenReturn(124L);
        when(nic2.getBytesSent()).thenReturn(457L);
        when(nic2.getPacketsRecv()).thenReturn(11L);
        when(nic2.getPacketsSent()).thenReturn(21L);
        when(nic2.getInErrors()).thenReturn(31L);
        when(nic2.getOutErrors()).thenReturn(41L);
        when(nic2.queryNetworkInterface()).thenReturn(networkInterface2);
        when(speedMeasurementManager.getCurrentSpeedForName("en2")).thenReturn(Optional.of(new SpeedMeasurementManager.CurrentSpeed(
                123L,
                321L
        )));

        when(hal.getNetworkIFs()).thenReturn(Arrays.asList(nic1, nic2));
    }

    @Test
    public void networkInterfacesHappyPath() {
        List<NetworkInterface> networkInterfaces = provider.networkInterfaces();

        assertThat(networkInterfaces.size(), is(2));
    }

    @Test
    public void networkLoadHappyPath() {
        List<NetworkInterfaceLoad> networkInterfaceLoads = provider.networkInterfaceLoads();
        assertThat(networkInterfaceLoads.size(), is(2));
    }

    @Test
    public void shouldHandleNoNicWithThatName() {
        Optional<NetworkInterface> en3 = provider.networkInterfaceById("en3");
        assertFalse(en3.isPresent());
    }

    @Test
    public void shouldHandleNoNicLoadWithThatName() {
        Optional<NetworkInterfaceLoad> en2 = provider.networkInterfaceLoadById("en3");
        assertFalse(en2.isPresent());
    }

    @Test
    public void shouldHandleJavaNetworkInterfaceThrowingExceptionsInIsUp() throws Exception {
        when(networkInterface1.isUp()).thenThrow(new SocketException("Shrug"));

        Optional<NetworkInterfaceLoad> en1 = provider.networkInterfaceLoadById("en0");
        assertTrue(en1.isPresent());
        assertFalse(en1.get().isUp());
    }

    @Test
    public void shouldHandleJavaNetworkInterfaceThrowingExceptionsInIsLoopBack() throws Exception {
        when(networkInterface1.isLoopback()).thenThrow(new SocketException("Shrug"));

        Optional<NetworkInterface> en1 = provider.networkInterfaceById("en0");
        assertTrue(en1.isPresent());
        assertFalse(en1.get().isLoopback());
    }
}