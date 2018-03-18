package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;

import java.util.Map;

public interface GpuInfoProvider {
    Gpu[] gpus();
    GpuInfo gpuInfo();
    Map<String, GpuHealth> gpuHealths();
}
