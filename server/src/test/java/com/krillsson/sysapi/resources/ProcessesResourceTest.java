package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.InfoProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ProcessesResourceTest {

    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new ProcessesResource(provider))
            .build();


    @Before
    public void setUp() {

    }

    @Test
    public void name() throws Exception {

    }
}
