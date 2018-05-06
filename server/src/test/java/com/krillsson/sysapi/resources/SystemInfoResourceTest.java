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

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link SystemResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemInfoResourceTest {

    /*private static final InfoProvider provider = mock(InfoProvider.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new SystemResource(provider))
            .build();

    @Before
    public void setUp() {
    }

    @Test
    public void getSystemHappyPath() throws Exception {
        OperatingSystem operatingSystem = mock(OperatingSystem.class);
        CentralProcessor centralProcessor = mock(CentralProcessor.class);
        GlobalMemory globalMemory = mock(GlobalMemory.class);
        when(provider.systemInfo()).thenReturn(
                new com.krillsson.sysapi.core.domain.system.SystemInfo("theHost", PlatformEnum.LINUX, operatingSystem,
                        new CpuInfo(centralProcessor, new CpuLoad(100, 0, new CoreLoad[]{}, cpuHealth, processCount, threadCount), new CpuHealth(new double[0], 120, 1000, 10)
                        ),
                        globalMemory,
                        new PowerSource[0]));

        final SystemInfo response = RESOURCES.getJerseyTest().target("/system")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SystemInfo.class);
        assertNotNull(response);
        assertEquals(response.getCpuInfo().getCpuLoad().getCpuLoadCountingTicks(), 100, 0);
    }

    @Test
    public void getSystemSadPath() throws Exception {
        when(provider.systemInfo()).thenThrow(new RuntimeException());

        Response systemInfo = RESOURCES.getJerseyTest().target("/system")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(systemInfo.getMonitorStatus(), 500);
    }

    @Test
    public void getJvmPropertiesWithCustomProperties() throws Exception {
        System.setProperty("theProperty", "hello");

        JvmProperties response = null;
        try {
            response = RESOURCES.getJerseyTest().target("/system/jvm")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(JvmProperties.class);
        } catch (Exception e) {}

        assertNotNull(response.getProperties().get("theProperty"));
    }

    @After
    public void tearDown() throws Exception {
        reset(provider);
    }
    */

}