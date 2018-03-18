package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;

import java.util.Collections;
import java.util.Map;

public class DefaultGpuInfoProvider implements GpuInfoProvider {

    @Override
    public Gpu[] gpus() {
        return new Gpu[0];
    }

    @Override
    public GpuInfo gpuInfo() {
        return null;
    }

    @Override
    public Map<String, GpuHealth> gpuHealths() {
        return Collections.emptyMap();
    }
}
