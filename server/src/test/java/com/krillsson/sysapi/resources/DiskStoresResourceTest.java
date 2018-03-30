package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.DiskSpeed;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;
import oshi.hardware.HWDiskStore;
import oshi.software.os.OSFileStore;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class DiskStoresResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new DiskStoresResource(provider))
            .build();
    private StorageInfo diskSd0;
    private DiskInfo diskInfo;

    @Before
    public void setUp() throws Exception {
        diskInfo = new DiskInfo(new HWDiskStore(), new DiskHealth(0, new HealthData[0]),
                new DiskSpeed(0,0), new OSFileStore("diskInfo", "", "", "", "", "", 0, 0));
        diskInfo.getHwDiskStore().setName("sd0");
        diskSd0 = new StorageInfo(new DiskInfo[]{diskInfo}, 0, 0);
    }

    @Test
    public void getStorageInfoHappyPath() throws Exception {
        when(provider.storageInfo())
                .thenReturn(diskSd0);

        final com.krillsson.sysapi.dto.storage.StorageInfo response = RESOURCES.getJerseyTest().target("/storage")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.storage.StorageInfo.class);

        assertNotNull(response);
        assertEquals(response.getDiskInfo().length, 1);
        assertThat(response.getDiskInfo()[0].getOsFileStore().getName(), is(equalToIgnoringCase("diskInfo")));
    }

    @Test
    public void getStorageInfoSadPath() throws Exception {
        when(provider.storageInfo())
                .thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/storage")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Assert.assertEquals(response.getStatus(), 500);
    }

    @Test
    public void getDiskInfoByNameDiskExists() throws Exception {
        Optional<DiskInfo> diskInfoOptional = Optional.of(this.diskInfo);
        when(provider.getDiskInfoByName("sd0")).thenReturn(diskInfoOptional);
        final com.krillsson.sysapi.dto.storage.DiskInfo response = RESOURCES.getJerseyTest().target(String.format("/storage/%s", "sd0"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.storage.DiskInfo.class);

        assertNotNull(response);
        assertEquals("sd0", response.getDiskStore().getName());
    }

    @Test
    public void getDiskInfoByNameDiskDoesNotExist() throws Exception {
        Optional<DiskInfo> type = Optional.ofNullable(null);
        when(provider.getDiskInfoByName("sd0")).thenReturn(type);
        Response response = RESOURCES.getJerseyTest().target(String.format("/storage/%s", "sd0"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(404, response.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}