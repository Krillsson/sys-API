package com.krillsson.sysapi.core.windows;

import ohmwrapper.MonitorManager;
import org.junit.Before;
import oshi.hardware.HardwareAbstractionLayer;

import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WindowsNetworkProviderTest {

    MonitorManager monitorManager;
    HardwareAbstractionLayer hal;

    @Before
    public void setUp() throws Exception {
        monitorManager = mock(MonitorManager.class);
    }
}