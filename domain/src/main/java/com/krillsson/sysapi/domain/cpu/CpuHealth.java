package com.krillsson.sysapi.domain.cpu;

public class CpuHealth {
    private final double[] temperatures;
    private final double voltage;
    private final double fanRpm;
    private final double fanPercent;

    public CpuHealth(double[] temperatures, double voltage, double fanRpm, double fanPercent) {
        this.temperatures = temperatures;
        this.voltage = voltage;
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
    }

    public double[] getTemperatures() {
        return temperatures;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getFanRpm() {
        return fanRpm;
    }

    public double getFanPercent() {
        return fanPercent;
    }
}
