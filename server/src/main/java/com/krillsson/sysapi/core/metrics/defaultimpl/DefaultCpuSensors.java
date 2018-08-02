package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Arrays;
import java.util.List;

public class DefaultCpuSensors {

    private final HardwareAbstractionLayer hal;

    public DefaultCpuSensors(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    protected CpuHealth cpuHealth() {
        List<Double> temperature = cpuTemperatures();
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

    protected double cpuVoltage() {
        return hal.getSensors().getCpuVoltage();
    }

    protected List<Double> cpuTemperatures() {
        return Arrays.asList(hal.getSensors().getCpuTemperature());
    }

    protected double cpuFanRpm() {
        return Arrays.stream(hal.getSensors().getFanSpeeds()).findFirst().orElse(0);
    }

    protected double cpuFanPercent() {
        return 0;
    }
}
