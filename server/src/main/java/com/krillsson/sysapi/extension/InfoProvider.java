package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

public interface InfoProvider {
    HWDiskHealth diskHealth(String name, HWDiskStore diskStore);

    double[] cpuTemperatures();

    double getCpuFanRpm();

    double getCpuFanPercent();
}
