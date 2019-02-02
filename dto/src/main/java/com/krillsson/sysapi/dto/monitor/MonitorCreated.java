package com.krillsson.sysapi.dto.monitor;

public class MonitorCreated {
    private final String id;

    public MonitorCreated(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}