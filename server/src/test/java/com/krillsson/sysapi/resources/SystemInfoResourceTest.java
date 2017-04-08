/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */

package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.core.DefaultInfoProvider;
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.dto.system.JvmProperties;
import com.krillsson.sysapi.dto.system.SystemInfo;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.platform.linux.LinuxCentralProcessor;
import oshi.hardware.platform.linux.LinuxGlobalMemory;
import oshi.software.os.OperatingSystem;
import oshi.software.os.linux.LinuxOperatingSystem;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SystemResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemInfoResourceTest {

    private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new SystemResource(provider))
            .build();

    @Before
    public void setUp() {
    }

    @Test
    public void getSystemHappyPath() throws Exception {
        when(provider.getSystemInfo()).thenReturn(
                new com.krillsson.sysapi.core.domain.system.SystemInfo("theHost",
                        new LinuxOperatingSystem(),
                new CpuInfo(new LinuxCentralProcessor(), 4, 80,
                        new CpuLoad(100,0,0,0,0, 0,0,0,0),
                        new CpuHealth(new double[0], 120, 1000, 10)),
                        new LinuxGlobalMemory(),
                        new PowerSource[0]));

        final SystemInfo response = RESOURCES.getJerseyTest().target("/system")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SystemInfo.class);
        assertNotNull(response);
        assertEquals(response.getCpuInfo().getCpuLoad().getCpuLoadCountingTicks(), 100, 0);
    }

    @Test
    public void getSystemSadPath() throws Exception {
        when(provider.getSystemInfo()).thenThrow(new RuntimeException());

        Response systemInfo = RESOURCES.getJerseyTest().target("/system")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(systemInfo.getStatus(), 500);
    }

    @Test
    public void getJvmPropertiesWithCustomProperties() throws Exception {
        System.setProperty("theProperty", "hello");

        JvmProperties response = null;
        try {
            response = RESOURCES.getJerseyTest().target("/system/jvm")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(JvmProperties.class);
        } catch (Exception e) {/* empty */}

        assertNotNull(response.getProperties().get("theProperty"));
    }

}