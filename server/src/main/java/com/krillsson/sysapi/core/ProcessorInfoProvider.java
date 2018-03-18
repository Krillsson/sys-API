package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import oshi.hardware.CentralProcessor;

public interface ProcessorInfoProvider {
    CpuInfo cpuInfo();
    CpuLoad cpuLoad();
}
