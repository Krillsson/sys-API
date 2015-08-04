package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.krillsson.sysapi.sigar.FilesystemSigar;

import static org.mockito.Mockito.mock;

public class FilesystemResourceTest {

    private static final FilesystemSigar filesystemMock = mock(FilesystemSigar.class);

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new FilesystemResource(filesystemMock))
            .build();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFilesystem() throws Exception {

    }
}