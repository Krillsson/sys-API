package com.krillsson.sysapi.rest;

import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.NetworkMetrics;
import com.krillsson.sysapi.util.OffsetDateTimeConverter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class NetworkInterfacesResourceTest {
    private static final NetworkMetrics provider = mock(NetworkMetrics.class);
    private static final MetricsHistoryManager historyManager = mock(MetricsHistoryManager.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new NetworkInterfacesResource(provider, historyManager))
            .addProvider(OffsetDateTimeConverter.class)
            .build();
    private com.krillsson.sysapi.core.domain.network.NetworkInterface networkInterfaceData;

    @Before
    public void setUp() throws Exception {
        networkInterfaceData = new com.krillsson.sysapi.core.domain.network.NetworkInterface(
                "en0",
                "",
                "",
                0, 0,
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Test
    public void shouldReturnNotFoundIfNoDataPresent() throws Exception {
        when(provider.networkInterfaceById("en1")).thenReturn(Optional.empty());
        Response response = RESOURCES.getJerseyTest().target("/nics/en1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void shouldReturnReasonableData() throws Exception {
        when(provider.networkInterfaceById("en0")).thenReturn(Optional.of(networkInterfaceData));
        com.krillsson.sysapi.dto.network.NetworkInterface networkInterfaceData = RESOURCES.getJerseyTest()
                .target("/nics/en0")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.network.NetworkInterface.class);
        assertThat(networkInterfaceData, is(networkInterfaceData));
    }

    @Test
    public void shouldReturnReasonableArrayData() throws Exception {
        when(provider.networkInterfaces()).thenReturn(Arrays.asList(networkInterfaceData));
        List<com.krillsson.sysapi.dto.network.NetworkInterface> networkInterfaceData = RESOURCES.getJerseyTest()
                .target("/nics")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<com.krillsson.sysapi.dto.network.NetworkInterface>>() {
                });
        assertThat(networkInterfaceData, is(networkInterfaceData));
    }


    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}