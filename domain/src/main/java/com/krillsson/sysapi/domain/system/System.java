package com.krillsson.sysapi.domain.system;

import com.krillsson.sysapi.domain.cpu.Cpu;
import oshi.json.hardware.ComputerSystem;
import oshi.json.hardware.GlobalMemory;
import oshi.json.hardware.PowerSource;
import oshi.json.software.os.OperatingSystem;

public class System {
    private final String hostName;
    private final OperatingSystem operatingSystem;
    private final Cpu cpu;
    private final GlobalMemory memory;
    private final PowerSource[] powerSources;

    public System(String hostName, OperatingSystem operatingSystem, Cpu cpu, GlobalMemory memory, PowerSource[] powerSources) {
        this.hostName = hostName;
        this.operatingSystem = operatingSystem;
        this.cpu = cpu;
        this.memory = memory;
        this.powerSources = powerSources;
    }

    public String getHostName() {
        return hostName;
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
}
