package com.krillsson.sysapi.core;

import org.junit.Before;
import org.junit.Test;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by krillsson on 2017-04-24.
 */
public class DefaultDiskProviderTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void name() throws Exception {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        HWDiskStore[] diskStores = hal.getDiskStores();
        //Arrays.stream(diskStores).forEach(hwDiskStore -> hwDiskStore.);
    }

}