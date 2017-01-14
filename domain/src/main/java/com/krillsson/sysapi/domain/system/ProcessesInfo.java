package com.krillsson.sysapi.domain.system;

import oshi.json.hardware.GlobalMemory;
import oshi.json.software.os.OSProcess;

public class ProcessesInfo {
    private final GlobalMemory memory;
    private final OSProcess[] processes;

    public ProcessesInfo(GlobalMemory memory, OSProcess[] processes) {
        this.memory = memory;
        this.processes = processes;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public OSProcess[] getProcesses() {
        return processes;
    }
}
