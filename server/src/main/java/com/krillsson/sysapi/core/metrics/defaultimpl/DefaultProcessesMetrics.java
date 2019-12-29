package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.core.metrics.ProcessesMetrics;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultProcessesMetrics implements ProcessesMetrics {
    private final OperatingSystem operatingSystem;
    private final HardwareAbstractionLayer hal;

    public DefaultProcessesMetrics(OperatingSystem operatingSystem, HardwareAbstractionLayer hal) {
        this.operatingSystem = operatingSystem;
        this.hal = hal;
    }

    @Override
    public ProcessesInfo processesInfo(OperatingSystem.ProcessSort sortBy, int limit) {
        MemoryLoad memory = memoryLoad();
        List<Process> processes = Collections.emptyList();
        if (limit > -1) {
            processes = Arrays
                    .stream(operatingSystem.getProcesses(limit, sortBy))
                    .map(p -> Process.create(p, memory))
                    .collect(Collectors.toList());
        }
        return new ProcessesInfo(
                memory,
                operatingSystem.getProcessId(),
                operatingSystem.getThreadCount(),
                operatingSystem.getProcessCount(),
                processes
        );
    }

    @Override
    public Optional<Process> getProcessByPid(int pid) {
        MemoryLoad memory = memoryLoad();
        return Optional
                .of(operatingSystem.getProcess(pid))
                .map((OSProcess p) -> Process.create(p, memory));
    }

    public MemoryLoad memoryLoad() {
        GlobalMemory memory = hal.getMemory();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        return new MemoryLoad(
                operatingSystem.getProcessCount(),
                virtualMemory.getSwapTotal(),
                virtualMemory.getSwapUsed(),
                memory.getTotal(),
                memory.getAvailable()
        );
    }
}

