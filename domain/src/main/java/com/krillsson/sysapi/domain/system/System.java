package com.krillsson.sysapi.domain.system;

import com.krillsson.sysapi.domain.cpu.Cpu;
import oshi.json.hardware.*;
import oshi.json.software.os.*;

public class System {
    private final OperatingSystem operatingSystem;
    private final ComputerSystem computerSystem;
    private final Cpu cpu;
    private final GlobalMemory memory;
    private final PowerSource[] powerSources;
    private final Sensors sensors;

    public System(OperatingSystem operatingSystem, ComputerSystem computerSystem, Cpu cpu, GlobalMemory memory, PowerSource[] powerSources, Sensors sensors) {
        this.operatingSystem = operatingSystem;
        this.computerSystem = computerSystem;
        this.cpu = cpu;
        this.memory = memory;
        this.powerSources = powerSources;
        this.sensors = sensors;
    }

    public ComputerSystem getComputerSystem() {
        return computerSystem;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public PowerSource[] getPowerSources() {
        return powerSources;
    }

    public Sensors getSensors() {
        return sensors;
    }
}
