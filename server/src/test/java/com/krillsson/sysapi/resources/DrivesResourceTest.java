package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.OsPartition;
import com.krillsson.sysapi.core.metrics.DriveMetrics;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DrivesResourceTest {
    private static final DriveMetrics provider = mock(DriveMetrics.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new DrivesResource(provider))
            .build();
    private Drive drive;

    @Before
    public void setUp() throws Exception {
        drive = new Drive(
                "",
                "",
                "",
                new OsPartition("", "", "", "", 0, 0, 0, "", "", "", "", "", 0, 0),
                Collections.emptyList()
        );
    }

    @Test
    public void getStorageInfoHappyPath() throws Exception {
        when(provider.drives())
                .thenReturn(Arrays.asList(drive));

        final com.krillsson.sysapi.dto.storage.StorageInfo response = RESOURCES.getJerseyTest().target("/drives")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.storage.StorageInfo.class);

        assertNotNull(response);
        assertEquals(response.getDiskInfo().length, 1);
        assertThat(response.getDiskInfo()[0].getOsFileStore().getName(), is(equalToIgnoringCase("drive")));
    }

    @Test
    public void getStorageInfoSadPath() throws Exception {
        when(provider.drives())
                .thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/drives")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Assert.assertEquals(response.getStatus(), 500);
    }

    @Test
    public void getDiskInfoByNameDiskExists() throws Exception {
        Optional<Drive> diskInfoOptional = Optional.of(this.drive);
        when(provider.driveByName("sd0")).thenReturn(diskInfoOptional);
        final com.krillsson.sysapi.dto.storage.DiskInfo response = RESOURCES.getJerseyTest()
                .target(String.format("/drives/%s", "sd0"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.storage.DiskInfo.class);

        assertNotNull(response);
        assertEquals("sd0", response.getDiskStore().getName());
    }

    @Test
    public void getDiskInfoByNameDiskDoesNotExist() throws Exception {
        Optional<Drive> type = Optional.ofNullable(null);
        when(provider.driveByName("sd0")).thenReturn(type);
        Response response = RESOURCES.getJerseyTest().target(String.format("/drives/%s", "sd0"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(404, response.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}