package com.krillsson.maintenance.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.krillsson.maintenance.sigar.SystemMetrics;

import static org.mockito.Mockito.mock;

public class SystemResourceTest {
    private static final SystemMetrics systemMock = mock(SystemMetrics.class);

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SystemResource(systemMock))
            .build();

    @Before
    public void setUp() throws Exception {
    }
    @Test
    public void testAll() throws Exception {

    }
}