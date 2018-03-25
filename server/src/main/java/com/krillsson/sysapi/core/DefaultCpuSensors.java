package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Arrays;

public class DefaultCpuSensors {

    private final HardwareAbstractionLayer hal;

    public DefaultCpuSensors(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    public CpuHealth cpuHealth() {
        double[] temperature = cpuTemperatures();
        double fanRpm = cpuFanRpm();
        double fanPercent = cpuFanPercent();
        double cpuVoltage = cpuVoltage();
        return new CpuHealth(
                temperature,
                cpuVoltage,
                fanRpm,
                fanPercent
        );
    }

    double cpuVoltage() {
        return hal.getSensors().getCpuVoltage();
    }

    double[] cpuTemperatures() {
        return new double[]{hal.getSensors().getCpuTemperature()};
    }

    double cpuFanRpm() {
        return Arrays.stream(hal.getSensors().getFanSpeeds()).findFirst().orElse(0);
    }

    double cpuFanPercent() {
        return 0;
    }
}
