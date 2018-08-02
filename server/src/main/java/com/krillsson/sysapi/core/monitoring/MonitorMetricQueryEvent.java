package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.query.MetricQueryEvent;

public class MonitorMetricQueryEvent extends MetricQueryEvent {

    public MonitorMetricQueryEvent(SystemLoad load) {
        super(load);
    }
}
