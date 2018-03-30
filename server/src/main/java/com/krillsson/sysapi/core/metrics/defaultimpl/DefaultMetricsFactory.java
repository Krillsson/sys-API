package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.MetricsFactory;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.metrics.*;
import com.krillsson.sysapi.util.Utils;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class DefaultMetricsFactory implements MetricsFactory {

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    private CpuMetrics cpuMetrics;
    private NetworkMetrics networkMetrics;
    private GpuMetrics gpuMetrics;
    private DiskMetrics diskMetrics;
    private ProcessesMetrics processesMetrics;
    private MotherboardMetrics motherboardMetrics;
    private MemoryMetrics memoryMetrics;

    public DefaultMetricsFactory(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SpeedMeasurementManager speedMeasurementManager, Utils utils) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = utils;
    }

    @Override
    public boolean prerequisitesFilled() {
        return true;
    }

    @Override
    public boolean initialize() {
        setCpuMetrics(new DefaultCpuMetrics(hal, operatingSystem, new DefaultCpuSensors(hal), utils));
        DefaultNetworkMetrics networkInfoProvider = new DefaultNetworkMetrics(hal, speedMeasurementManager);
        networkInfoProvider.register();
        setNetworkMetrics(networkInfoProvider);
        setGpuMetrics(new DefaultGpuMetrics(hal));
        DefaultDiskProvider diskInfoProvider = new DefaultDiskProvider(operatingSystem, hal, speedMeasurementManager);
        diskInfoProvider.register();
        setDiskMetrics(diskInfoProvider);
        setProcessesMetrics(new DefaultProcessesMetrics(operatingSystem, hal));
        setMotherboardMetrics(new DefaultMotherboardMetrics(hal));
        setMemoryMetrics(new DefaultMemoryMetrics(hal));
        return true;
    }

    @Override
    public CpuMetrics cpuInfoProvider() {
        return cpuMetrics;
    }

    @Override
    public NetworkMetrics networkInfoProvider() {
        return networkMetrics;
    }

    @Override
    public DiskMetrics diskInfoProvider() {
        return diskMetrics;
    }

    @Override
    public MemoryMetrics memoryInfoProvider() {
        return memoryMetrics;
    }

    @Override
    public ProcessesMetrics processesInfoProvider() {
        return processesMetrics;
    }

    @Override
    public GpuMetrics gpuInfoProvider() {
        return gpuMetrics;
    }

    @Override
    public MotherboardMetrics motherboardInfoProvider() {
        return motherboardMetrics;
    }

    protected void setCpuMetrics(CpuMetrics cpuMetrics) {
        this.cpuMetrics = cpuMetrics;
    }

    protected void setNetworkMetrics(NetworkMetrics networkMetrics) {
        this.networkMetrics = networkMetrics;
    }

    protected void setGpuMetrics(GpuMetrics gpuMetrics) {
        this.gpuMetrics = gpuMetrics;
    }

    protected void setDiskMetrics(DiskMetrics diskMetrics) {
        this.diskMetrics = diskMetrics;
    }

    protected void setProcessesMetrics(ProcessesMetrics processesMetrics) {
        this.processesMetrics = processesMetrics;
    }

    protected void setMotherboardMetrics(MotherboardMetrics motherboardMetrics) {
        this.motherboardMetrics = motherboardMetrics;
    }

    protected void setMemoryMetrics(MemoryMetrics memoryMetrics) {
        this.memoryMetrics = memoryMetrics;
    }
}
