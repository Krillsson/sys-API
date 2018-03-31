package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import ohmwrapper.Bandwidth;
import ohmwrapper.MonitorManager;
import ohmwrapper.NetworkMonitor;
import ohmwrapper.NicInfo;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WindowsNetworkProviderTest {

    MonitorManager monitorManager;
    NetworkMonitor networkMonitor;
    HardwareAbstractionLayer hal;
    NetworkIF networkIF;
    NicInfo nicInfo;
    Bandwidth inBandwidth;
    Bandwidth outBandwidth;

    WindowsNetworkMetrics nicProvider;

    @Before
    public void setUp() throws Exception {
        monitorManager = mock(MonitorManager.class);
        networkMonitor = mock(NetworkMonitor.class);
        when(monitorManager.getNetworkMonitor()).thenReturn(networkMonitor);
        hal = mock(HardwareAbstractionLayer.class);
        networkIF = mock(NetworkIF.class);
        nicInfo = mock(NicInfo.class);
        inBandwidth = mock(Bandwidth.class);
        outBandwidth = mock(Bandwidth.class);
        when(nicInfo.getInBandwidth()).thenReturn(inBandwidth);
        when(nicInfo.getOutBandwidth()).thenReturn(outBandwidth);
        when(inBandwidth.getValue()).thenReturn(123d);
        when(outBandwidth.getValue()).thenReturn(321d);

        nicProvider = new WindowsNetworkMetrics(hal, mock(SpeedMeasurementManager.class), monitorManager);
    }

    @Test
    public void happyPath() throws Exception {
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[]{networkIF});
        when(networkIF.getName()).thenReturn("en0");
        when(networkIF.getMacaddr()).thenReturn("01:23:45:67:89:ab");
        when(networkMonitor.getNics()).thenReturn(new NicInfo[]{nicInfo});
        when(nicInfo.getPhysicalAddress()).thenReturn("01:23:45:67:89:AB");

        Optional<NetworkInterfaceLoad> en0Optional = nicProvider.networkInterfaceLoadById("en0");
        NetworkInterfaceLoad en0 = en0Optional.get();
        assertThat(en0.getSpeed().getReceiveBytesPerSecond(), is(123L));
        assertThat(en0.getSpeed().getSendBytesPerSecond(), is(321L));
        verify(monitorManager).Update();
    }

    @Test(expected = NoSuchElementException.class)
    public void noAvailableSpeedRecordsReturnsDefaultValues() throws Exception {
        when(hal.getNetworkIFs()).thenReturn(new NetworkIF[0]);
        assertFalse(nicProvider.networkInterfaceLoadById("en0").isPresent());
    }
}