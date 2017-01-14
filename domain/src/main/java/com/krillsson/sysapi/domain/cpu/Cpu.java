package com.krillsson.sysapi.domain.cpu;

import oshi.json.hardware.CentralProcessor;
import oshi.json.hardware.Sensors;

public class Cpu {
    private final CentralProcessor centralProcessor;
    private final double[] temperatures;
    private final double voltage;
    private final double fanRpm;
    private final double fanPercent;

    public Cpu(CentralProcessor centralProcessor, double voltage, double fanRpm, double fanPercent, double[] temperatures) {
        this.centralProcessor = centralProcessor;
        this.voltage = voltage;
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.temperatures = temperatures;
    }

    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
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
