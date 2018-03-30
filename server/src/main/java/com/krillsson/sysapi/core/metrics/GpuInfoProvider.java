package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;

import java.util.List;
import java.util.Map;

public interface GpuInfoProvider {
    GpuInfo gpuInfo();
    List<GpuLoad> gpuLoads();
}
