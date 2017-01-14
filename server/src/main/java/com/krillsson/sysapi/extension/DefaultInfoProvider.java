package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.HealthData;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public double cpuFanRpm() {
        return 0;
    }

    @Override
    public double cpuFanPercent() {
        return 0;
    }

    @Override
    public List<HealthData> healthData() {
        return Collections.emptyList();
    }

    @Override
    public Gpu[] gpus() {
        return new Gpu[0];
    }
}
