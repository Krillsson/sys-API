package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.metrics.*;

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
