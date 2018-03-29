package com.krillsson.sysapi.core;

public interface InfoProviderFactoryFactory {
    boolean prerequisitesFilled();

    boolean initialize();

    CpuInfoProvider cpuInfoProvider();

    NetworkInfoProvider networkInfoProvider();

    DiskInfoProvider diskInfoProvider();

    MemoryInfoProvider memoryInfoProvider();

    ProcessesInfoProvider processesInfoProvider();

    GpuInfoProvider gpuInfoProvider();

    MotherboardInfoProvider motherboardInfoProvider();
}
