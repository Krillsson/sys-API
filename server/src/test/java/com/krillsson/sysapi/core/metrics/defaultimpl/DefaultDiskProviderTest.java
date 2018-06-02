package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import org.junit.Before;
import org.junit.Test;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDiskProviderTest {

    String DEV_SDA1 = "/dev/sda1";
    String DEV_SDA2 = "/dev/sda2";
    DefaultDriveProvider provider;
    SpeedMeasurementManager measurementManager;
    OperatingSystem os;
    FileSystem fs;
    HardwareAbstractionLayer hal;
    String osPartitionDisk1Uuid = UUID.randomUUID().toString();
    String osPartitionDisk2Uuid = UUID.randomUUID().toString();

    HWDiskStore disk1;
    HWPartition disk1Partition1;
    HWPartition disk1Partition2;
    OSFileStore disk1OsPartition;

    HWDiskStore disk2;
    HWPartition disk2Partition1;
    HWPartition disk2Partition2;
    OSFileStore disk2OsPartition;

    @Before
    public void setUp() throws Exception {
        measurementManager = mock(SpeedMeasurementManager.class);
        os = mock(OperatingSystem.class);
        fs = mock(FileSystem.class);
        hal = mock(HardwareAbstractionLayer.class);
        provider = new DefaultDriveProvider(os, hal, measurementManager);

        disk1 = mock(HWDiskStore.class);
        when(disk1.getName()).thenReturn(DEV_SDA1);
        disk1Partition1 = mock(HWPartition.class);
        when(disk1Partition1.getUuid()).thenReturn(UUID.randomUUID().toString());
        disk1Partition2 = mock(HWPartition.class);
        when(disk1Partition2.getUuid()).thenReturn(osPartitionDisk1Uuid);
        disk1OsPartition = mock(OSFileStore.class);
        when(disk1OsPartition.getUUID()).thenReturn(osPartitionDisk1Uuid);
        when(disk1.getPartitions()).thenReturn(new HWPartition[]{disk1Partition1, disk1Partition2});
        when(measurementManager.getCurrentSpeedForName(DEV_SDA1)).thenReturn(Optional.of(new SpeedMeasurementManager.CurrentSpeed(
                1000,
                1000
        )));

        disk2 = mock(HWDiskStore.class);
        when(disk2.getName()).thenReturn(DEV_SDA2);
        disk2Partition1 = mock(HWPartition.class);
        when(disk2Partition1.getUuid()).thenReturn(UUID.randomUUID().toString());
        disk2Partition2 = mock(HWPartition.class);
        when(disk2Partition2.getUuid()).thenReturn(osPartitionDisk2Uuid);
        disk2OsPartition = mock(OSFileStore.class);
        when(disk2OsPartition.getUUID()).thenReturn(osPartitionDisk2Uuid);
        when(disk2.getPartitions()).thenReturn(new HWPartition[]{disk2Partition1, disk2Partition2});
        when(measurementManager.getCurrentSpeedForName(DEV_SDA2)).thenReturn(Optional.of(new SpeedMeasurementManager.CurrentSpeed(
                1234,
                1234
        )));

        when(os.getFileSystem()).thenReturn(fs);
        when(hal.getDiskStores()).thenReturn(new HWDiskStore[]{disk1, disk2});

        when(os.getFileSystem().getFileStores()).thenReturn(new OSFileStore[]{disk1OsPartition, disk2OsPartition});
        provider.register();
    }


    @Test
    public void diskLoadHappyPath() {
        List<DriveLoad> driveLoads = provider.driveLoads();

        assertThat(driveLoads.size(), is(2));
        assertThat(driveLoads.get(0).getSpeed().getReadBytesPerSecond(), is(1000L));
    }

    @Test
    public void shouldReturnAllDrivesPossible() {
        List<Drive> drives = provider.drives();

        assertThat(drives.size(), is(2));
        assertEquals(drives.get(0).getDiskOsPartition().getUuid(), osPartitionDisk1Uuid);
    }

    @Test
    public void shouldHandleDrivesNotMappingToOsDrive() {
        when(disk1Partition2.getUuid()).thenReturn(UUID.randomUUID().toString());

        List<Drive> drives = provider.drives();

        assertNotNull(drives.get(0).getDiskOsPartition());
    }

    @Test
    public void shouldHandleNoDrivesWithThatName() {
        Optional<Drive> diskInfo = provider.driveByName("/dev/sda3");
        assertFalse(diskInfo.isPresent());
    }

    @Test
    public void shouldHandleNoLoadForDriveWithThatName() {
        Optional<DriveLoad> diskInfo = provider.driveLoadByName("/dev/sda3");
        assertFalse(diskInfo.isPresent());
    }

    @Test
    public void shouldHandleSpeedMeasurementManagerReturningEmpty() {
        when(measurementManager.getCurrentSpeedForName(DEV_SDA1)).thenReturn(Optional.empty());
        Optional<DriveLoad> diskInfo = provider.driveLoadByName(DEV_SDA1);
        assertTrue(diskInfo.isPresent());
    }
}
