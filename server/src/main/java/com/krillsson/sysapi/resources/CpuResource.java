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

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper;
import com.krillsson.sysapi.dto.cpu.CpuLoad;
import io.dropwizard.auth.Auth;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    public static final int SLEEP_SAMPLE_PERIOD = 500;
    private final OperatingSystem operatingSystem;
    private final Sensors sensors;
    private final CentralProcessor processor;
    private final InfoProvider provider;

    private long[] ticks = new long[0];
    private long sampledAt = -1;

    public CpuResource(OperatingSystem operatingSystem, Sensors sensors, CentralProcessor processor, InfoProvider provider) {
        this.operatingSystem = operatingSystem;
        this.sensors = sensors;
        this.processor = processor;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.cpu.CpuInfo getRoot(@Auth UserConfiguration user) {
        CpuLoad cpuLoad = createCpuLoad();

        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.cpuFanRpm();
        double fanPercent = provider.cpuFanPercent();
        if (temperature.length == 0) {
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        printCpu(processor);
        return CpuInfoMapper.INSTANCE.map(new CpuInfo(processor,
                operatingSystem.getProcessCount(),
                operatingSystem.getThreadCount(),
                new CpuHealth(temperature,
                        sensors.getCpuVoltage(),
                        fanRpm,
                        fanPercent)));
    }

    private CpuLoad createCpuLoad() {
        //If this is the first time the method is run we need to get some sample data
        if (Arrays.equals(ticks, new long[0]) || isOutsideSamplingDuration()) {
            ticks = processor.getSystemCpuLoadTicks();
            sampledAt = currentSystemTime();
            Util.sleep(SLEEP_SAMPLE_PERIOD);
        }
        long[] currentTicks = processor.getSystemCpuLoadTicks();
        long user = currentTicks[TickType.USER.getIndex()] - ticks[TickType.USER.getIndex()];
        long nice = currentTicks[TickType.NICE.getIndex()] - ticks[TickType.NICE.getIndex()];
        long sys = currentTicks[TickType.SYSTEM.getIndex()] - ticks[TickType.SYSTEM.getIndex()];
        long idle = currentTicks[TickType.IDLE.getIndex()] - ticks[TickType.IDLE.getIndex()];
        long iowait = currentTicks[TickType.IOWAIT.getIndex()] - ticks[TickType.IOWAIT.getIndex()];
        long irq = currentTicks[TickType.IRQ.getIndex()] - ticks[TickType.IRQ.getIndex()];
        long softirq = currentTicks[TickType.SOFTIRQ.getIndex()] - ticks[TickType.SOFTIRQ.getIndex()];

        long totalCpu = user + nice + sys + idle + iowait + irq + softirq;

        System.out.format("CPU load: %.1f%% (counting ticks)%n", processor.getSystemCpuLoadBetweenTicks() * 100);
        System.out.format("CPU load: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad() * 100);
        return new CpuLoad(processor.getSystemCpuLoadBetweenTicks() * 100,
                processor.getSystemCpuLoad() * 100,
                100d * user / totalCpu,
                100d * nice / totalCpu,
                100d * sys / totalCpu,
                100d * idle / totalCpu,
                100d * iowait / totalCpu,
                100d * irq / totalCpu,
                100d * softirq / totalCpu);
    }

    @GET
    @Path("ticks")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public long[] getTicks(@Auth UserConfiguration user) {
        return processor.getSystemCpuLoadTicks();
    }

    private static void printCpu(CentralProcessor processor) {



        double[] loadAverage = processor.getSystemLoadAverage(3);
        System.out.println("CPU load averages:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
                + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
                + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
        // per core CPU
        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks();
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        System.out.println(procCpu.toString());
    }

    private boolean isOutsideSamplingDuration() {
        return currentSystemTime() - sampledAt > MAX_SAMPLING_THRESHOLD;
    }

    private static long currentSystemTime() {
        return System.currentTimeMillis();
    }

}
