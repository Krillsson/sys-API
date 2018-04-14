package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.history.History;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class Monitor<T, R> {
    //TODO inertia of monitored items: i.e for how long must the threshold be reached before it's an actual
    //TODO event

    //id to monitor
    private final String id;
    private final R threshold;
    private boolean isAboveThreshold;

    public Monitor(String id, R threshold) {
        this.id = id;
        this.threshold = threshold;
    }



    Optional<MonitorEvent> check(History<T> history){
        for (Map.Entry<LocalDateTime, T> historyItem : history.get().entrySet()) {

            // how do we implement this when we need to consider the inertia?
            // how do we handle inertia that has higher resolution then gap between measurement points?
            if(isAboveThreshold(valueToMonitor(historyItem.getValue())) ){

            }
        }
    }

    abstract Duration inertia();

    abstract R valueToMonitor(T item);

    abstract boolean isAboveThreshold(R value);

    abstract boolean isNearThreshold(R value);
}
