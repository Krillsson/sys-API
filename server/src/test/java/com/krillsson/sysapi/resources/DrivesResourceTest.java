package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.OsPartition;
import com.krillsson.sysapi.core.history.HistoryManager;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.DriveMetrics;
import com.krillsson.sysapi.dto.network.NetworkInterface;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DrivesResourceTest {
    private static final DriveMetrics provider = mock(DriveMetrics.class);
    private static final MetricsHistoryManager historyManager = mock(MetricsHistoryManager.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new DrivesResource(provider, historyManager))
            .build();
    private Drive drive;

    @Before
    public void setUp() throws Exception {
        drive = new Drive(
                "",
                "sd0",
                "",
                new OsPartition("", "", "", "", 0, 0, 0, "", "", "", "", "", 0, 0),
                Collections.emptyList()
        );
    }

    @Test
    public void getStorageInfoHappyPath() throws Exception {
        when(provider.drives())
                .thenReturn(Arrays.asList(drive));

        final List<com.krillsson.sysapi.dto.drives.Drive> response = RESOURCES.getJerseyTest().target("/drives")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<com.krillsson.sysapi.dto.drives.Drive>>() {
                });

        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertThat(response.get(0).getName(), is(equalToIgnoringCase("sd0")));
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
        final com.krillsson.sysapi.dto.drives.Drive response = RESOURCES.getJerseyTest()
                .target(String.format("/drives/%s", "sd0"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.drives.Drive.class);

        assertNotNull(response);
        assertEquals("sd0", response.getName());
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