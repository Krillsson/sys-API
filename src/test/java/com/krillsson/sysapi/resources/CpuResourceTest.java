package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.krillsson.sysapi.sigar.CpuSigar;

import static org.mockito.Mockito.mock;

public class CpuResourceTest {

    private static final CpuSigar cpuMock = mock(CpuSigar.class);

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CpuResource(cpuMock))
            .build();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAll() throws Exception {

    }
}