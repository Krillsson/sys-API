package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.dto.network.NetworkInterfaceData;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import oshi.hardware.NetworkIF;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class NetworkInterfacesResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new NetworkInterfacesResource(provider))
            .build();
    private com.krillsson.sysapi.core.domain.network.NetworkInterfaceData networkInterfaceData;

    @Before
    public void setUp() throws Exception {
        networkInterfaceData = new com.krillsson.sysapi.core.domain.network.NetworkInterfaceData(getNetworkIf(), new NetworkInterfaceSpeed(1, 1));
    }

    @Test
    public void shouldReturnNotFoundIfNoDataPresent() throws Exception {
        when(provider.getNetworkInterfaceById("en1")).thenReturn(Optional.empty());
        Response response = RESOURCES.getJerseyTest().target("/nics/en1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void shouldReturnReasonableData() throws Exception {
        when(provider.getNetworkInterfaceById("en0")).thenReturn(Optional.of(networkInterfaceData));
        NetworkInterfaceData networkInterfaceData = RESOURCES.getJerseyTest().target("/nics/en0")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(NetworkInterfaceData.class);
        assertTrue(networkInterfaceData.getNetworkInterfaceSpeed().getRxbps()>= 0);
    }

    @Test
    public void shouldReturnReasonableArrayData() throws Exception {
        when(provider.getAllNetworkInterfaces()).thenReturn(new com.krillsson.sysapi.core.domain.network.NetworkInterfaceData[]{networkInterfaceData});
        NetworkInterfaceData[] networkInterfaceData = RESOURCES.getJerseyTest().target("/nics")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(NetworkInterfaceData[].class);
        assertTrue(networkInterfaceData[0].getNetworkInterfaceSpeed().getRxbps()>= 0);
    }

    private NetworkIF getNetworkIf() {

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(interfaces)) {
                if (!netint.isLoopback() && netint.getHardwareAddress() != null) {
                    NetworkIF netIF = new NetworkIF();
                    netIF.setNetworkInterface(netint);
                    netIF.updateNetworkStats();
                    //return first reasonable netIF instance
                    return netIF;
                }
            }
        } catch (SocketException ignored) {
        }
        return null;
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}