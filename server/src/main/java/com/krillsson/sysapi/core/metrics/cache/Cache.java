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
        this.cpuMetrics = new CachingCpuMetrics(provider.cpuMetrics());
        this.networkMetrics = new CachingNetworkMetrics(provider.networkMetrics());
        this.gpuMetrics = new CachingGpuMetrics(provider.gpuMetrics());
        this.driveMetrics = new CachingDriveMetrics(provider.driveMetrics());
        this.processesMetrics = new CachingProcessesMetrics(provider.processesMetrics());
        this.motherboardMetrics = new CachingMotherboardMetrics(provider.motherboardMetrics());
        this.memoryMetrics = new CachingMemoryMetrics(provider.memoryMetrics());
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
    public CpuMetrics cpuMetrics() {
        return cpuMetrics;
    }

    @Override
    public NetworkMetrics networkMetrics() {
        return networkMetrics;
    }

    @Override
    public DriveMetrics driveMetrics() {
        return driveMetrics;
    }

    @Override
    public MemoryMetrics memoryMetrics() {
        return memoryMetrics;
    }

    @Override
    public ProcessesMetrics processesMetrics() {
        return processesMetrics;
    }

    @Override
    public GpuMetrics gpuMetrics() {
        return gpuMetrics;
    }

    @Override
    public MotherboardMetrics motherboardMetrics() {
        return motherboardMetrics;
    }
}
