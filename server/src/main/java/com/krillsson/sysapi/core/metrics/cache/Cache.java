package com.krillsson.sysapi.core.metrics.cache;

import com.codahale.metrics.Metric;
import com.krillsson.sysapi.core.metrics.*;

public class Cache implements MetricsFactory {
    private final CpuMetrics cpuMetrics;
    private final NetworkMetrics networkMetrics;
    private final GpuMetrics gpuMetrics;
    private final DriveMetrics driveMetrics;
    private final ProcessesMetrics processesMetrics;
    private final MotherboardMetrics motherboardMetrics;
    private final MemoryMetrics memoryMetrics;

    public static MetricsFactory wrap(MetricsFactory factory)
    {
        return new Cache(factory);
    }

    private Cache(MetricsFactory provider)
    {
        this.cpuMetrics = new CachingCpuMetrics(provider.cpuInfoProvider());
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
