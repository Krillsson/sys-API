package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.dto.processes.ProcessInfo;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessesResourceTest {

    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new ProcessesResource(provider))
            .build();
    private Process process;


    @Before
    public void setUp() {

        process = new Process(
                "name",
                "path",
                "commandLine",
                "user",
                "123",
                "group",
                "1233",
                OSProcess.State.RUNNING,
                100,
                99,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
    }

    @Test
    public void getProcessesBadSortingMethod() throws Exception {
        try {
            RESOURCES.getJerseyTest().target("/processes")
                    .queryParam("sortBy", "RAM")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(WebApplicationException.class);
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), 400);
        }
    }

    @Test
    public void getProcessesBadlyFormattedLimit() throws Exception {
        try {
            RESOURCES.getJerseyTest().target("/processes")
                    .queryParam("limit", "-100")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(WebApplicationException.class);
            fail("Should have thrown WebApplicationException");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }

    @Test
    public void getProcessesCorrectLimit() throws Exception {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        when(provider.processesInfo(any(OperatingSystem.ProcessSort.class), captor.capture()))
                .thenReturn(new ProcessesInfo(mock(GlobalMemory.class), 0, 0, 0,
                        new Process[]{process}));
        ProcessInfo processInfo = RESOURCES.getJerseyTest().target("/processes")
                .queryParam("limit", "10")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(ProcessInfo.class);
        assertEquals(captor.getValue(), new Integer(10));
        assertThat(processInfo.getProcesses()[0].getName(), is(equalToIgnoringCase("name")));
        assertThat(processInfo.getProcesses()[0].getState(), is(equalToIgnoringCase(process.getState().name())));
    }

    @Test
    public void getProcessesCorrectSortingMethod() throws Exception {
        ArgumentCaptor<OperatingSystem.ProcessSort> captor = ArgumentCaptor.forClass(OperatingSystem.ProcessSort.class);
        when(provider.processesInfo(captor.capture(), anyInt()))
                .thenReturn(new ProcessesInfo(mock(GlobalMemory.class), 0, 0, 0,
                        new Process[]{process}));
        ProcessInfo processInfo = RESOURCES.getJerseyTest().target("/processes")
                .queryParam("sortBy", "MEMORY")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(ProcessInfo.class);
        assertEquals(captor.getValue(), OperatingSystem.ProcessSort.MEMORY);
        assertThat(processInfo.getProcesses()[0].getName(), is(equalToIgnoringCase("name")));
        assertThat(processInfo.getProcesses()[0].getState(), is(equalToIgnoringCase(process.getState().name())));
    }

    @Test
    public void getProcessesHappyPath() throws Exception {
        when(provider.processesInfo(any(OperatingSystem.ProcessSort.class), anyInt()))
                .thenReturn(new ProcessesInfo(mock(GlobalMemory.class), 0, 0, 0,
                        new Process[]{process}));

        final ProcessInfo response = RESOURCES.getJerseyTest().target("/processes")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(ProcessInfo.class);
        assertNotNull(response);
        assertEquals(response.getProcesses().length, 1);
        assertThat(response.getProcesses()[0].getName(), is(equalToIgnoringCase("name")));
        assertThat(response.getProcesses()[0].getState(), is(equalToIgnoringCase(process.getState().name())));
    }

    @Test
    public void getProcessesSadPath() throws Exception {
        when(provider.processesInfo(any(OperatingSystem.ProcessSort.class), anyInt())).thenThrow(new RuntimeException("What"));
        final Response response = RESOURCES.getJerseyTest().target("/processes")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Assert.assertEquals(response.getStatus(), 500);
    }
}
