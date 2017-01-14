package com.krillsson.sysapi.domain.cpu;

import oshi.json.hardware.CentralProcessor;

public class Cpu {
    private final CentralProcessor centralProcessor;
    private final int processCount;
    private final int threadCount;
    private final CpuHealth cpuHealth;

    public Cpu(CentralProcessor centralProcessor, int processCount, int threadCount, CpuHealth cpuHealth) {
        this.centralProcessor = centralProcessor;
        this.processCount = processCount;
        this.threadCount = threadCount;
        this.cpuHealth = cpuHealth;
    }

    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
    }

    public int getProcessCount() {
        return processCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }
}
