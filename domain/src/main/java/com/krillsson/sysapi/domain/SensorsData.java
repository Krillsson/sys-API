package com.krillsson.sysapi.domain;

import java.util.List;

public class SensorsData {
    private final double[] cpuTemperatures;
    private final double cpuFanRpm;
    private final double cpuFanPercent;
    private final List<HealthData> healthDatas;

    public SensorsData(double[] cpuTemperatures, double cpuFanRpm, double cpuFanPercent, List<HealthData> healthDatas) {

        this.cpuTemperatures = cpuTemperatures;
        this.cpuFanRpm = cpuFanRpm;
        this.cpuFanPercent = cpuFanPercent;
        this.healthDatas = healthDatas;
    }

    public double[] getCpuTemperatures() {
        return cpuTemperatures;
    }

    public double getCpuFanRpm() {
        return cpuFanRpm;
    }

    public double getCpuFanPercent() {
        return cpuFanPercent;
    }

    public List<HealthData> getHealthDatas() {
        return healthDatas;
    }
}
