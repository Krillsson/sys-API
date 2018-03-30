package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.ProcessesMetrics;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
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
        GlobalMemory memory = hal.getMemory();
        List<Process> processes = Arrays
                .stream(operatingSystem.getProcesses(limit, sortBy))
                .map(p -> Process.create(p, memory))
                .collect(Collectors.toList());

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
        return Optional
                .of(operatingSystem.getProcess(pid))
                .map((OSProcess p) -> Process.create(p, hal.getMemory()));
    }
}
