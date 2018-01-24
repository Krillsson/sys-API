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

import com.krillsson.sysapi.core.domain.cpu.CoreLoad;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.sensors.SensorsInfo;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;
import com.krillsson.sysapi.core.domain.system.SystemInfo;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultInfoProvider.class);

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final Utils utils;
    private final DefaultNetworkProvider defaultNetworkProvider;
    private final DefaultDiskProvider defaultDiskProvider;

    private long[] ticks = new long[0];
    private long ticksSampledAt = -1;

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 500;
    private long coreTicksSampledAt = -1;
    private long[][] coreTicks = new long[0][0];

    protected DefaultInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils, DefaultNetworkProvider defaultNetworkProvider, DefaultDiskProvider defaultDiskProvider) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.utils = utils;
        this.defaultNetworkProvider = defaultNetworkProvider;
        this.defaultDiskProvider = defaultDiskProvider;
    }

    @Override
    public CpuInfo cpuInfo() {
        double[] temperature = cpuTemperatures();
        double fanRpm = cpuFanRpm();
        double fanPercent = cpuFanPercent();
        CpuLoad cpuLoad = cpuLoad();
        return new CpuInfo(
                processor(),
                operatingSystem.getProcessCount(),
                operatingSystem.getThreadCount(),
                cpuLoad,
                new CpuHealth(
                        temperature,
                        cpuVoltage(),
                        fanRpm,
                        fanPercent));
    }

    @Override
    public CentralProcessor processor() {
        return hal.getProcessor();
    }

    @Override
    public double cpuVoltage() {
        return hal.getSensors().getCpuVoltage();
    }

    @Override
    public long[] systemCpuLoadTicks() {
        return processor().getSystemCpuLoadTicks();
    }

    @Override
    protected boolean canProvide() {
        return true;
    }

    @Override
    public DiskHealth diskHealth(String name) {
        return defaultDiskProvider.diskHealth(name);
    }

    @Override
    public double[] cpuTemperatures() {
        return new double[]{hal.getSensors().getCpuTemperature()};
    }

    @Override
    public double cpuFanRpm() {
        return Arrays.stream(hal.getSensors().getFanSpeeds()).findFirst().orElse(0);
    }

    @Override
    public double cpuFanPercent() {
        return 0;
    }

    @Override
    public HealthData[] mainboardHealthData() {
        return new HealthData[0];
    }

    @Override
    public Gpu[] gpus() {
        return new Gpu[0];
    }

    @Override
    public Map<String, GpuHealth> gpuHealths() {
        return Collections.emptyMap();
    }

    public CpuLoad cpuLoad() {
        CentralProcessor processor = processor();
        if (Arrays.equals(coreTicks, new long[0][0]) || utils.isOutsideMaximumDuration(coreTicksSampledAt, MAX_SAMPLING_THRESHOLD)) {
            LOGGER.debug("Sleeping thread since we don't have enough sample data. Hold on!");
            coreTicks = processor.getProcessorCpuLoadTicks();
            coreTicksSampledAt = utils.currentSystemTime();
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }
        CoreLoad[] coreLoads = new CoreLoad[processor.getLogicalProcessorCount()];
        long[][] currentProcessorTicks = processor.getProcessorCpuLoadTicks();
        long sampledAt = utils.currentSystemTime();
        for (int i = 0; i < coreLoads.length; i++) {
            long[] currentTicks = currentProcessorTicks[i];
            long user = currentTicks[CentralProcessor.TickType.USER.getIndex()] - coreTicks[i][CentralProcessor.TickType.USER.getIndex()];
            long nice = currentTicks[CentralProcessor.TickType.NICE.getIndex()] - coreTicks[i][CentralProcessor.TickType.NICE.getIndex()];
            long sys = currentTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - coreTicks[i][CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = currentTicks[CentralProcessor.TickType.IDLE.getIndex()] - coreTicks[i][CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = currentTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - coreTicks[i][CentralProcessor.TickType.IOWAIT.getIndex()];
            long irq = currentTicks[CentralProcessor.TickType.IRQ.getIndex()] - coreTicks[i][CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = currentTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - coreTicks[i][CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = currentTicks[CentralProcessor.TickType.STEAL.getIndex()] - coreTicks[i][CentralProcessor.TickType.STEAL.getIndex()];

            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
            long totalIdle = idle + iowait;
            long totalSystem = irq + softirq + sys + steal;


        }

        coreTicks = currentProcessorTicks;
        coreTicksSampledAt = utils.currentSystemTime();

    }

    @Override
    public CpuLoad cpuLoad() {
        //If this is the first time the method is run we need to get some sample data
        CentralProcessor processor = processor();
        if (Arrays.equals(ticks, new long[0]) || utils.isOutsideMaximumDuration(ticksSampledAt, MAX_SAMPLING_THRESHOLD)) {
            LOGGER.debug("Sleeping thread since we don't have enough sample data. Hold on!");
            ticks = processor.getSystemCpuLoadTicks();
            ticksSampledAt = utils.currentSystemTime();
            utils.sleep(SLEEP_SAMPLE_PERIOD);
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
                BigDecimal.valueOf(processor.getSystemCpuLoadBetweenTicks() * 100d).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(processor.getSystemCpuLoad() * 100d).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * user / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * nice / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * sys / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * idle / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * iowait / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * irq / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(100d * softirq / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()
        );
    }

    @Override
    public SystemInfo systemInfo() {
        double[] temperature = cpuTemperatures();
        double fanRpm = cpuFanRpm();
        double fanPercent = cpuFanPercent();
        CpuLoad cpuLoad = cpuLoad();
        if (temperature.length == 0) {
            temperature = new double[]{hal.getSensors().getCpuTemperature()};
        }
        return new SystemInfo(
                getHostName(),
                oshi.SystemInfo.getCurrentPlatformEnum(), operatingSystem,
                new CpuInfo(processor(),
                        operatingSystem.getProcessCount(),
                        operatingSystem.getThreadCount(),
                        cpuLoad, new CpuHealth(
                        temperature,
                        cpuVoltage(),
                        fanRpm,
                        fanPercent)),
                hal.getMemory(),
                hal.getPowerSources());
    }

    @Override
    public OperatingSystem operatingSystem() {
        return operatingSystem;
    }

    private String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    @Override
    public NetworkInterfaceData[] getAllNetworkInterfaces() {
        return defaultNetworkProvider.getAllNetworkInterfaces();
    }

    @Override
    public Optional<NetworkInterfaceData> getNetworkInterfaceById(String id) {
        return defaultNetworkProvider.getNetworkInterfaceById(id);
    }

    @Override
    public Optional<NetworkInterfaceSpeed> getNetworkInterfaceSpeed(String id) {
        return defaultNetworkProvider.getSpeed(id);
    }

    @Override
    public String[] getNetworkInterfaceNames() {
        return defaultNetworkProvider.getNetworkInterfaceNames();
    }

    @Override
    public ProcessesInfo processesInfo(OperatingSystem.ProcessSort sortBy, int limit) {
        GlobalMemory memory = hal.getMemory();
        Process[] processes = Arrays
                .stream(operatingSystem.getProcesses(limit, sortBy))
                .map(p -> Process.create(p, memory))
                .collect(Collectors.toList())
                .toArray(new Process[0]);

        return new ProcessesInfo(memory, operatingSystem.getProcessId(), operatingSystem.getThreadCount(), operatingSystem.getProcessCount(), processes);
    }

    @Override
    public Optional<Process> getProcessByPid(int pid) {
        return Optional
                .of(operatingSystem.getProcess(pid))
                .map((OSProcess p) -> Process.create(p, hal.getMemory()));
    }

    @Override
    public SensorsInfo sensorsInfo() {
        double[] cpuTemperatures = cpuTemperatures();
        double cpuFanRpm = cpuFanRpm();
        double cpuFanPercent = cpuFanPercent();
        if (cpuTemperatures.length == 0) {
            cpuTemperatures = new double[]{hal.getSensors().getCpuTemperature()};
        }
        HealthData[] healthData = mainboardHealthData();
        return new SensorsInfo(new CpuHealth(cpuTemperatures, cpuVoltage(), cpuFanRpm, cpuFanPercent), gpuHealths(), healthData);
    }

    @Override
    public Optional<DiskInfo> getDiskInfoByName(String name) {
        return defaultDiskProvider.getDiskInfoByName(name);
    }

    @Override
    public GlobalMemory globalMemory() {
        return hal.getMemory();
    }

    @Override
    public PowerSource[] powerSources() {
        return hal.getPowerSources();
    }

    @Override
    public GpuInfo gpuInfo() {
        return new GpuInfo(hal.getDisplays(), gpus());
    }

    @Override
    public Motherboard motherboard() {
        return new Motherboard(hal.getComputerSystem(), hal.getUsbDevices(false));
    }

    @Override
    public StorageInfo storageInfo() {
        return defaultDiskProvider.storageInfo();
    }

}
