package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.query.MetricQueryEvent;

public class HistoryMetricQueryEvent extends MetricQueryEvent {

    public HistoryMetricQueryEvent(SystemLoad load) {
        super(load);
    }
}
