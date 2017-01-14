package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    @Override
    protected boolean canProvide() {
        return true;
    }

    @Override
    public HWDiskHealth diskHealth(String name, HWDiskStore diskStore) {
        return null;
    }

    @Override
    public double[] cpuTemperatures() {
        return new double[0];
    }

    @Override
    public double getCpuFanRpm() {
        return 0;
    }

    @Override
    public double getCpuFanPercent() {
        return 0;
    }
}
