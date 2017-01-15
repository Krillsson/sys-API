package com.krillsson.sysapi.core;

import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.gpu.GpuHealth;
import com.krillsson.sysapi.domain.health.HealthData;
import com.krillsson.sysapi.domain.storage.HWDiskHealth;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    @Override
    protected boolean canProvide() {
        return true;
    }

    @Override
    public HWDiskHealth diskHealth(String name) {
        return null;
    }

    @Override
    public double[] cpuTemperatures() {
        return new double[0];
    }

    @Override
    public double cpuFanRpm() {
        return 0;
    }

    @Override
    public double cpuFanPercent() {
        return 0;
    }

    @Override
    public HealthData[] healthData() {
        return new HealthData[0];
    }

    @Override
    public Gpu[] gpus() {
        return new Gpu[0];
    }

    @Override
    public Map<String, GpuHealth> gpuHealths() {
        return Collections.emptyMap();
    }
}
