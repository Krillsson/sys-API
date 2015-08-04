package com.krillsson.hwapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.krillsson.hwapi.sigar.CpuMetrics;

import static org.mockito.Mockito.mock;

public class CpuResourceTest {

    private static final CpuMetrics cpuMock = mock(CpuMetrics.class);

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