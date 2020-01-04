package com.krillsson.sysapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetaInfoResourceTest {

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new MetaInfoResource("1.0", new String[]{"test0", "test1"}, 100))
            .build();

    @Test
    public void getRootHappyPath() throws Exception {
        final com.krillsson.sysapi.dto.metadata.Meta response = RESOURCES.getJerseyTest().target("/meta")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(com.krillsson.sysapi.dto.metadata.Meta.class);

        assertNotNull(response);
        assertEquals(response.getVersion(), "1.0");
        assertEquals(response.getEndpoints()[0], "test0");
        assertEquals(response.getEndpoints()[1], "test1");
    }

    @Test
    public void getVersionHappyPath() throws Exception {
        final String response = RESOURCES.getJerseyTest().target("meta/version")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        assertNotNull(response);
        assertEquals(response, "1.0");
    }
}