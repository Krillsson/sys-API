package com.krillsson.sysapi.core.metrics.cache;

import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.metrics.*;

public class Cache implements Metrics {
    private final CpuMetrics cpuMetrics;
    private final NetworkMetrics networkMetrics;
    private final GpuMetrics gpuMetrics;
    private final DriveMetrics driveMetrics;
    private final ProcessesMetrics processesMetrics;
    private final MotherboardMetrics motherboardMetrics;
    private final MemoryMetrics memoryMetrics;

    private Cache(Metrics provider, CacheConfiguration cacheConfiguration) {
        this.cpuMetrics = new CachingCpuMetrics(provider.cpuMetrics(), cacheConfiguration);
        this.networkMetrics = new CachingNetworkMetrics(provider.networkMetrics(), cacheConfiguration);
        this.gpuMetrics = new CachingGpuMetrics(provider.gpuMetrics(), cacheConfiguration);
        this.driveMetrics = new CachingDriveMetrics(provider.driveMetrics(), cacheConfiguration);
        this.processesMetrics = new CachingProcessesMetrics(provider.processesMetrics(), cacheConfiguration);
        this.motherboardMetrics = new CachingMotherboardMetrics(provider.motherboardMetrics(), cacheConfiguration);
        this.memoryMetrics = new CachingMemoryMetrics(provider.memoryMetrics(), cacheConfiguration);
    }

    public static Metrics wrap(Metrics factory, CacheConfiguration cacheConfiguration) {
        return new Cache(factory, cacheConfiguration);
    }

    @Override
    public void initialize() {

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
