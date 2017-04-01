package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import static org.mockito.Mockito.mock;

public class ProcessesResourceTest {

    static OperatingSystem os = mock(OperatingSystem.class);
    static GlobalMemory memory = mock(GlobalMemory.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new ProcessesResource(os, memory))
            .build();


}
