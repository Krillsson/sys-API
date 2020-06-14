package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.monitoring.Monitor;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkUpMonitorTest {

    SystemLoad systemLoad;
    NetworkInterfaceLoad networkInterfaceLoad;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        networkInterfaceLoad = mock(NetworkInterfaceLoad.class);
        when(systemLoad.getNetworkInterfaceLoads()).thenReturn(Arrays.asList(networkInterfaceLoad));
        when(networkInterfaceLoad.getName()).thenReturn("en0");
    }

    @Test
    public void monitorValuesCorrectly() {
        when(networkInterfaceLoad.isUp()).thenReturn(true);
        NetworkUpMonitor networkUpMonitor = new NetworkUpMonitor(UUID.randomUUID(), new MonitorConfig("en0", 1, Duration.ZERO));

        assertTrue(networkUpMonitor.check(systemLoad));

        when(networkInterfaceLoad.isUp()).thenReturn(false);
        assertFalse(networkUpMonitor.check(systemLoad));
    }

    @Test
    public void tryingToMonitorNonExistentNicDoesNotBreak() {
        when(networkInterfaceLoad.getName()).thenReturn("en1");

        NetworkUpMonitor networkUpMonitor = new NetworkUpMonitor(UUID.randomUUID(), new MonitorConfig("en0", 1, Duration.ZERO));

        assertFalse(networkUpMonitor.check(systemLoad));
    }
}