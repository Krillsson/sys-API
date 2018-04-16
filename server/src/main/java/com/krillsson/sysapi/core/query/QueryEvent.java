package com.krillsson.sysapi.core.query;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

public class QueryEvent {
    private final SystemLoad load;

    public QueryEvent(SystemLoad load) {
        this.load = load;
    }

    public SystemLoad load() {
        return load;
    }
}
