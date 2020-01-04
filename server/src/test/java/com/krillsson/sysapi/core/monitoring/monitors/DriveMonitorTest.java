package com.krillsson.sysapi.core.monitoring.monitors;

import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.drives.DriveValues;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DriveMonitorTest {
    SystemLoad systemLoad;
    DriveLoad driveLoad;
    DriveValues driveValues;

    @Before
    public void setUp() throws Exception {
        systemLoad = mock(SystemLoad.class);
        driveLoad = mock(DriveLoad.class);
        driveValues = mock(DriveValues.class);
        when(driveLoad.getName()).thenReturn("sd0");
        when(driveLoad.getValues()).thenReturn(driveValues);
    }

    @Test
    public void monitorValuesCorrectly() {
        when(systemLoad.getDriveLoads()).thenReturn(Arrays.asList(driveLoad));
        DriveMonitor driveMonitor = new DriveMonitor("sd0", Duration.ofSeconds(0), 1024);

        when(driveValues.getUsableSpace()).thenReturn(512L);
        assertTrue(driveMonitor.isOutsideThreshold(driveMonitor.value(systemLoad)));

        when(driveValues.getUsableSpace()).thenReturn(2048L);
        assertFalse(driveMonitor.isOutsideThreshold(driveMonitor.value(systemLoad)));
    }

    @Test
    public void tryingToMonitorNonExistentDriveDoesNotBreak() {
        when(driveLoad.getName()).thenReturn("sd1");

        DriveMonitor driveMonitor = new DriveMonitor("sd0", Duration.ofSeconds(0), 1024);

        assertTrue(driveMonitor.isOutsideThreshold(driveMonitor.value(systemLoad)));
    }
}