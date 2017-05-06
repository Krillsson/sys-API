package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.InfoProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import oshi.hardware.GlobalMemory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class MemoryResourceTest {
    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new MemoryResource(provider))
            .build();

    @Test
    public void getMemoryHappyPath() throws Exception {
        GlobalMemory memory = mock(GlobalMemory.class);
        when(memory.getAvailable()).thenReturn(100L);
        when(memory.getSwapTotal()).thenReturn(100L);
        when(memory.getTotal()).thenReturn(100L);
        when(provider.globalMemory()).thenReturn(memory);

        final com.krillsson.sysapi.dto.memory.GlobalMemory response = RESOURCES.getJerseyTest().target("/memory")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.memory.GlobalMemory.class);
        assertNotNull(response);
        assertEquals(response.getAvailable(), 100, 0);
    }

    @Test
    public void getMemorySadPath() throws Exception {
        when(provider.globalMemory()).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/memory")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(response.getStatus(), 500);
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
}