package com.krillsson.sysapi.core.query;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

public class MetricQueryEvent {

    private final SystemLoad load;

    public MetricQueryEvent(SystemLoad load) {
        this.load = load;
    }

    public SystemLoad load() {
        return load;
    }
}
