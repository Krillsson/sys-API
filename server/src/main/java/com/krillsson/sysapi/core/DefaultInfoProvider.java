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
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider
{

    private final HardwareAbstractionLayer hal;
    private long[] ticks = new long[0];
    private long sampledAt = -1;

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 500;

    public DefaultInfoProvider(HardwareAbstractionLayer hal)
    {
        this.hal = hal;
    }

    @Override
    protected boolean canProvide()
    {
        return true;
    }

    @Override
    public DiskHealth diskHealth(String name)
    {
        return null;
    }

    @Override
    public double[] cpuTemperatures()
    {
        return new double[0];
    }

    @Override
    public double cpuFanRpm()
    {
        return 0;
    }

    @Override
    public double cpuFanPercent()
    {
        return 0;
    }

    @Override
    public HealthData[] mainboardHealthData()
    {
        return new HealthData[0];
    }

    @Override
    public Gpu[] gpus()
    {
        return new Gpu[0];
    }

    @Override
    public Map<String, GpuHealth> gpuHealths()
    {
        return Collections.emptyMap();
    }

    @Override
    public CpuLoad cpuLoad()
    {
        //If this is the first time the method is run we need to get some sample data
        CentralProcessor processor = hal.getProcessor();
        if (Arrays.equals(ticks, new long[0]) || isOutsideSamplingDuration())
        {
            ticks = processor.getSystemCpuLoadTicks();
            sampledAt = currentSystemTime();
            Util.sleep(SLEEP_SAMPLE_PERIOD);
        }
        long[] currentTicks = processor.getSystemCpuLoadTicks();
        long user = currentTicks[CentralProcessor.TickType.USER.getIndex()] - ticks[CentralProcessor.TickType.USER.getIndex()];
        long nice = currentTicks[CentralProcessor.TickType.NICE.getIndex()] - ticks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = currentTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - ticks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = currentTicks[CentralProcessor.TickType.IDLE.getIndex()] - ticks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = currentTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - ticks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = currentTicks[CentralProcessor.TickType.IRQ.getIndex()] - ticks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = currentTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()];

        long totalCpu = user + nice + sys + idle + iowait + irq + softirq;

        return new CpuLoad(
                processor.getSystemCpuLoadBetweenTicks() * 100,
                processor.getSystemCpuLoad() * 100,
                100d * user / totalCpu,
                100d * nice / totalCpu,
                100d * sys / totalCpu,
                100d * idle / totalCpu,
                100d * iowait / totalCpu,
                100d * irq / totalCpu,
                100d * softirq / totalCpu
        );
    }

    private boolean isOutsideSamplingDuration() {
        return currentSystemTime() - sampledAt > MAX_SAMPLING_THRESHOLD;
    }

    private static long currentSystemTime() {
        return System.currentTimeMillis();
    }
}
