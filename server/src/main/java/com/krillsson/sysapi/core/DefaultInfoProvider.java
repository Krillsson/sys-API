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
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
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
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultInfoProvider.class);

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final Utils utils;
    private final DefaultNetworkProvider defaultNetworkProvider;

    private long[] ticks = new long[0];
    private long ticksSampledAt = -1;

    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 500;

    public DefaultInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.utils = utils;
        this.defaultNetworkProvider = new DefaultNetworkProvider(hal, utils);
    }

    @Override
    public CpuInfo cpuInfo() {
        double[] temperature = cpuTemperatures();
        double fanRpm = cpuFanRpm();
        double fanPercent = cpuFanPercent();
        CpuLoad cpuLoad = cpuLoad();
        if (temperature.length == 0) {
            temperature = new double[]{hal.getSensors().getCpuTemperature()};
        }
        return new CpuInfo(hal.getProcessor(),
                operatingSystem.getProcessCount(),
                operatingSystem.getThreadCount(),
                cpuLoad, new CpuHealth(temperature,
                hal.getSensors().getCpuVoltage(),
                fanRpm,
                fanPercent));
    }

    @Override
    public long[] systemCpuLoadTicks() {
        return hal.getProcessor().getSystemCpuLoadTicks();
    }

    @Override
    protected boolean canProvide() {
        return true;
    }

    @Override
    public DiskHealth diskHealth(String name) {
        return null;
    }

    @Override
    public double[] cpuTemperatures() {
        return new double[0];
    }

    @Override
    public double cpuFanRpm() {
        return 0;
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

    @Override
    public CpuLoad cpuLoad() {
        //If this is the first time the method is run we need to get some sample data
        CentralProcessor processor = hal.getProcessor();
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
                new BigDecimal(processor.getSystemCpuLoadBetweenTicks() * 100d).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(processor.getSystemCpuLoad() * 100d).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * user / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * nice / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * sys / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * idle / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * iowait / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * irq / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
                new BigDecimal(100d * softirq / totalCpu).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()
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
                operatingSystem,
                new CpuInfo(hal.getProcessor(),
                        operatingSystem.getProcessCount(),
                        operatingSystem.getThreadCount(),
                        cpuLoad, new CpuHealth(
                        temperature,
                        hal.getSensors().getCpuVoltage(),
                        fanRpm,
                        fanPercent)),
                hal.getMemory(),
                hal.getPowerSources());
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
        return new SensorsInfo(new CpuHealth(cpuTemperatures, hal.getSensors().getCpuVoltage(), cpuFanRpm, cpuFanPercent), gpuHealths(), healthData);
    }

    @Override
    public Optional<DiskInfo> getDiskInfoByName(String name){
        return Arrays.stream(hal.getDiskStores()).filter(d -> d.getName().equals(name)).map(di -> {
            OSFileStore associatedFileStore = findAssociatedFileStore(di);
            String mount = associatedFileStore != null ? associatedFileStore.getMount() : "N/A";
            return new DiskInfo(di, diskHealth(mount), associatedFileStore);
        }).findFirst();

    }

    @Override
    public StorageInfo storageInfo() {
        List<DiskInfo> diskInfos = new ArrayList<>();
        for (HWDiskStore diskStore : hal.getDiskStores()) {
            OSFileStore associatedFileStore = findAssociatedFileStore(diskStore);
            String name = associatedFileStore != null ? associatedFileStore.getMount() : "N/A";
            diskInfos.add(new DiskInfo(diskStore, diskHealth(name), associatedFileStore));
        }
        FileSystem fileSystem = operatingSystem.getFileSystem();
        return new StorageInfo(diskInfos.toArray(/*type reference*/new DiskInfo[0]), fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors(), utils.currentSystemTime());
    }

    private OSFileStore findAssociatedFileStore(HWDiskStore diskStore) {
        for (OSFileStore osFileStore : Arrays.asList(operatingSystem.getFileSystem().getFileStores())) {
            for (HWPartition hwPartition : Arrays.asList(diskStore.getPartitions())) {
                if (osFileStore.getUUID().equals(hwPartition.getUuid())) {
                    return osFileStore;
                }
            }
        }
        return null;
    }

}
