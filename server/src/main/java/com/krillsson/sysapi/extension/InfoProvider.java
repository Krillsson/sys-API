package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.HealthData;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

import java.util.List;
import java.util.Map;

public interface InfoProvider {

    HWDiskHealth diskHealth(String name, HWDiskStore diskStore);

    double[] cpuTemperatures();

    double cpuFanRpm();

    double cpuFanPercent();

    List<HealthData> healthData();

    Gpu[] gpus();
}
