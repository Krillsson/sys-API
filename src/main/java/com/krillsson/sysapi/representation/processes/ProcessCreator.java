package com.krillsson.sysapi.representation.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.ProcCredName;

public class ProcessCreator {
    private String user;
    private String group;

    public ProcessCreator(String user, String group) {
        this.user = user;
        this.group = group;
    }

    public ProcessCreator() {
        this.user = "N/A";
        this.group = "N/A";
    }

    public static ProcessCreator fromSigarBean(ProcCredName procCredName){
        return new ProcessCreator(procCredName.getUser(), procCredName.getGroup());
    }

    @JsonProperty
    public String getUser() {
        return user;
    }

    @JsonProperty
    public String getGroup() {
        return group;
    }
}
