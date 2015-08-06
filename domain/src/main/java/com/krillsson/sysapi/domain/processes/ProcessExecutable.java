package com.krillsson.sysapi.domain.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.ProcExe;

public class ProcessExecutable {
    String name;
    String cwd;

    public ProcessExecutable(String name, String cwd) {
        this.name = name;
        this.cwd = cwd;
    }

    public ProcessExecutable() {
        this.name = "N/A";
        this.cwd = "N/A";
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getCwd() {
        return cwd;
    }
}
