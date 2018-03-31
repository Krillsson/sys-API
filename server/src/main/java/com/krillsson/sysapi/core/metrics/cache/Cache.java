package com.krillsson.sysapi.core.metrics.cache;

import com.krillsson.sysapi.core.metrics.*;

public class Cache implements MetricsFactory {
    private final CpuMetrics cpuMetrics;
    private final NetworkMetrics networkMetrics;
    private final GpuMetrics gpuMetrics;
    private final DriveMetrics driveMetrics;
    private final ProcessesMetrics processesMetrics;
    private final MotherboardMetrics motherboardMetrics;
    private final MemoryMetrics memoryMetrics;

    private Cache(MetricsFactory provider) {
        this.cpuMetrics = new CachingCpuMetrics(provider.cpuInfoProvider());
        this.networkMetrics = new CachingNetworkMetrics(provider.networkInfoProvider());
        this.gpuMetrics = new CachingGpuMetrics(provider.gpuInfoProvider());
        this.driveMetrics = new CachingDriveMetrics(provider.diskInfoProvider());
        this.processesMetrics = new CachingProcessesMetrics(provider.processesInfoProvider());
        this.motherboardMetrics = new CachingMotherboardMetrics(provider.motherboardInfoProvider());
        this.memoryMetrics = new CachingMemoryMetrics(provider.memoryInfoProvider());
    }

    public static MetricsFactory wrap(MetricsFactory factory) {
        return new Cache(factory);
    }

    @Override
    public boolean prerequisitesFilled() {
        return true;
    }

    @Override
    public boolean initialize() {
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
    public DriveMetrics diskInfoProvider() {
        return driveMetrics;
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
}
