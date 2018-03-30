package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.*;
import com.krillsson.sysapi.core.metrics.*;
import com.krillsson.sysapi.util.Utils;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class DefaultInfoProviderFactory {

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SystemApiConfiguration configuration;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    private CpuInfoProvider cpuInfoProvider;
    private NetworkInfoProvider networkInfoProvider;
    private GpuInfoProvider gpuInfoProvider;
    private DiskInfoProvider diskInfoProvider;
    private ProcessesInfoProvider processesInfoProvider;
    private MotherboardInfoProvider motherboardInfoProvider;
    private MemoryInfoProvider memoryInfoProvider;

    public DefaultInfoProviderFactory(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SystemApiConfiguration configuration, SpeedMeasurementManager speedMeasurementManager, Utils utils) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.configuration = configuration;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = utils;
    }

    public boolean prerequisitesFilled() {
        return true;
    }

    public boolean initialize() {
        setCpuInfoProvider(new DefaultCpuInfoProvider(hal, operatingSystem, new DefaultCpuSensors(hal), utils));
        DefaultNetworkProvider networkInfoProvider = new DefaultNetworkProvider(hal, speedMeasurementManager);
        networkInfoProvider.register();
        setNetworkInfoProvider(networkInfoProvider);
        setGpuInfoProvider(new DefaultGpuInfoProvider(hal));
        DefaultDiskProvider diskInfoProvider = new DefaultDiskProvider(operatingSystem, hal, speedMeasurementManager);
        diskInfoProvider.register();
        setDiskInfoProvider(diskInfoProvider);
        setProcessesInfoProvider(new DefaultProcessesInfoProvider(operatingSystem, hal));
        setMotherboardInfoProvider(new DefaultMotherboardInfoProvider(hal));
        setMemoryInfoProvider(new DefaultMemoryInfoProvider(hal));
        return true;
    }

    public CpuInfoProvider cpuInfoProvider() {
        return cpuInfoProvider;
    }

    public NetworkInfoProvider networkInfoProvider() {
        return networkInfoProvider;
    }

    public DiskInfoProvider diskInfoProvider() {
        return diskInfoProvider;
    }

    public MemoryInfoProvider memoryInfoProvider() {
        return memoryInfoProvider;
    }

    public ProcessesInfoProvider processesInfoProvider() {
        return processesInfoProvider;
    }

    public GpuInfoProvider gpuInfoProvider() {
        return gpuInfoProvider;
    }

    public MotherboardInfoProvider motherboardInfoProvider() {
        return motherboardInfoProvider;
    }

    protected void setCpuInfoProvider(CpuInfoProvider cpuInfoProvider) {
        this.cpuInfoProvider = cpuInfoProvider;
    }

    protected void setNetworkInfoProvider(NetworkInfoProvider networkInfoProvider) {
        this.networkInfoProvider = networkInfoProvider;
    }

    protected void setGpuInfoProvider(GpuInfoProvider gpuInfoProvider) {
        this.gpuInfoProvider = gpuInfoProvider;
    }

    protected void setDiskInfoProvider(DiskInfoProvider diskInfoProvider) {
        this.diskInfoProvider = diskInfoProvider;
    }

    protected void setProcessesInfoProvider(ProcessesInfoProvider processesInfoProvider) {
        this.processesInfoProvider = processesInfoProvider;
    }

    protected void setMotherboardInfoProvider(MotherboardInfoProvider motherboardInfoProvider) {
        this.motherboardInfoProvider = motherboardInfoProvider;
    }

    protected void setMemoryInfoProvider(MemoryInfoProvider memoryInfoProvider) {
        this.memoryInfoProvider = memoryInfoProvider;
    }
}
