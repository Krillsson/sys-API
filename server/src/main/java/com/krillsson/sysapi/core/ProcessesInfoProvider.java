package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import oshi.software.os.OperatingSystem;

import java.util.Optional;

public interface ProcessesInfoProvider {
    ProcessesInfo processesInfo(OperatingSystem.ProcessSort sortBy, int limit);
    Optional<Process> getProcessByPid(int pid);
}
