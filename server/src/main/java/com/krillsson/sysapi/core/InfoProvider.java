package com.krillsson.sysapi.core;

import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.gpu.GpuHealth;
import com.krillsson.sysapi.domain.health.HealthData;
import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

import java.util.List;

public interface InfoProvider {

    HWDiskHealth diskHealth(String name, HWDiskStore diskStore);

    double[] cpuTemperatures();

    double cpuFanRpm();

    double cpuFanPercent();

    List<HealthData> healthData();

    Gpu[] gpus();

    GpuHealth[] gpuHealths();
}
