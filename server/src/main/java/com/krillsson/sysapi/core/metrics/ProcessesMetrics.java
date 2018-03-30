package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import oshi.software.os.OperatingSystem;

import java.util.Optional;

public interface ProcessesMetrics {
    ProcessesInfo processesInfo(OperatingSystem.ProcessSort sortBy, int limit);
    Optional<Process> getProcessByPid(int pid);
}
