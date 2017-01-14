package com.krillsson.sysapi.domain.health;

import com.krillsson.sysapi.domain.cpu.CpuHealth;
import com.krillsson.sysapi.domain.gpu.GpuHealth;

import java.util.List;

public class SensorsData {
    private final CpuHealth cpuHealth;
    private final GpuHealth[] gpuHealths;
    private final List<HealthData> healthDatas;

    public SensorsData(CpuHealth cpuHealth, GpuHealth[] gpuHealths, List<HealthData> healthDatas) {
        this.cpuHealth = cpuHealth;
        this.gpuHealths = gpuHealths;
        this.healthDatas = healthDatas;
    }

    public GpuHealth[] getGpuHealths() {
        return gpuHealths;
    }

    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    public List<HealthData> getHealthDatas() {
        return healthDatas;
    }
}
