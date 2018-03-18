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

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
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

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultInfoProvider.class);

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final DefaultNetworkProvider defaultNetworkProvider;
    private final DefaultDiskProvider defaultDiskProvider;

    private final Utils utils;
    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 1000;
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
    public long[] systemCpuLoadTicks() {
        return processor().getSystemCpuLoadTicks();
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
        return defaultDiskProvider.diskInfos();
    }

}
