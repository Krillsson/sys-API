package com.krillsson.sysapi.core;

import org.junit.Before;
import org.junit.Test;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import static org.mockito.Mockito.mock;

public class DefaultDiskProviderTest {

    DefaultDiskProvider provider;
    SpeedMeasurementManager measurementManager;
    OperatingSystem os;
    HardwareAbstractionLayer hal;
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
        hal = mock(HardwareAbstractionLayer.class);
        provider = new DefaultDiskProvider(os, hal, measurementManager);
    }

    @Test
    public void name() throws Exception {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        HWDiskStore[] diskStores = hal.getDiskStores();
        //Arrays.stream(diskStores).forEach(hwDiskStore -> hwDiskStore.);
    }

    @Test
    public void shouldReturnAllDrivesPossible() {

    }

    @Test
    public void shouldHandleDrivesNotMappingToOsDrive() {

    }

    @Test
    public void shouldHandleNoDrivesWithThatName() {
        //de061eee-0e4f-420c-8ccb-50da7b699ed5
        //54807373-8c7b-4edd-9988-8dc3ebb4182d
    }

   /* private HWDiskStore createDiskStore(String name, HWPartition[] partitions) {
        return new HWDiskStore(
                name,
                "HDD2000",
                "HD2-1234",
                1024,
                200,
                40,
                100,
                300,
                System.currentTimeMillis(),
                partitions,
                System.currentTimeMillis()
        );
    }

    private HWPartition createHwPartition(String device, String index, String uuid) {
        return new HWPartition(String.format("/%s/sda%s", device, index), "sda" + index, "ext4", uuid, 100, 9, 1, "/");
    }

    private OSFileStore createOsFileStore(String device, String index) {
        "name": "/",
                "volume": "/dev/sda1",
                "mount": "/",
                "description": "Local Disk",
                "uuid": "de061eee-0e4f-420c-8ccb-50da7b699ed5",
                "usableSpace": 61554176000,
                "totalSpace": 109291692032,
                "type": "ext4"
        return new OSFileStore("/", String.format("/%s/sda%s", device, index), "/", "Local Disk",)
    }*/
}
