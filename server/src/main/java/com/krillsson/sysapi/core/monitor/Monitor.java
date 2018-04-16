package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class Monitor<T, R> {
    private final String id;
    //id to monitor
    private final Duration inertia;
    private boolean steppedOverThreshold;
    private LocalDateTime steppedOverAt;

    public Monitor(String id, Duration inertia) {
        this.id = id;
        this.inertia = inertia;
    }

    Optional<MonitorEvent> check(SystemLoad event) {

        LocalDateTime now = LocalDateTime.now();

        T value = value(event);

        if(isAboveThreshold(value)){
            if(!steppedOverThreshold){
                steppedOverThreshold = true;
                steppedOverAt = now;
            }

            if(Duration.between(now, steppedOverAt).compareTo(inertia) > 0){

            }
        }












        //TODO this isn't inertia
        //TODO inertia: if we detect a raised value. It has to be rasied for x duration before we warn.
        if (isAboveThreshold(value)) {
            if (!steppedOverThreshold) {
                steppedOverThreshold = true;
                return Optional.of(new MonitorEvent<>(
                        now,
                        MonitorEvent.Severity.CRITICAL,
                        MonitorEvent.Type.START,
                        average
                ));
            } else {
                /*return Optional.of(new MonitorEvent<>(
                        now,
                        MonitorEvent.Severity.CRITICAL,
                        MonitorEvent.Type.ONGOING,
                        average
                ));*/
                return Optional.empty();
            }
        } else if (isNearThreshold(average)) {
            return Optional.of(new MonitorEvent<>(
                    now,
                    MonitorEvent.Severity.WARNING,
                    MonitorEvent.Type.STANDALONE,
                    average
            ));
        } else if (steppedOverThreshold) {
            steppedOverThreshold = false;
            return Optional.of(new MonitorEvent<>(now, MonitorEvent.Severity.NONE, MonitorEvent.Type.STOP, average));
        } else {
            return Optional.empty();
        }

    }

    public String getId() {
        return id;
    }

    protected abstract T value(SystemLoad systemLoad);

    protected abstract boolean isAboveThreshold(T value);

    protected abstract boolean isNearThreshold(T value);
}
