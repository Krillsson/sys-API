package com.krillsson.sysapi.domain.cpu;

import oshi.json.hardware.CentralProcessor;
import oshi.json.hardware.Sensors;

public class Cpu {
    private final CentralProcessor centralProcessor;
    private final Sensors sensors;

    public Cpu(CentralProcessor centralProcessor, Sensors sensors) {
        this.centralProcessor = centralProcessor;
        this.sensors = sensors;
    }

    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
    }

    public Sensors getSensors() {
        return sensors;
    }
}
