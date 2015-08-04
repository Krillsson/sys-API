package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.krillsson.sysapi.sigar.SystemSigar;

import static org.mockito.Mockito.mock;

public class SystemResourceTest {
    private static final SystemSigar systemMock = mock(SystemSigar.class);

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