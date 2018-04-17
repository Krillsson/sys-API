package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.Duration.between;

public abstract class Monitor<T, R> {
    private final String id;
    //id to monitor
    private final Duration inertia;
    private LocalDateTime stateChangedAt = null;
    private State state;

    enum State {
        BELOW,
        ABOVE_BEFORE_INERTIA,
        ABOVE,
        BELOW_BEFORE_INERTIA
    }

    public Monitor(String id, Duration inertia) {
        this.id = id;
        this.inertia = inertia;
    }

    Optional<MonitorEvent> check(SystemLoad systemLoad) {

        LocalDateTime now = LocalDateTime.now();

        T value = value(systemLoad);
        boolean aboveThreshold = isAboveThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(now, /* and */ stateChangedAt).compareTo(inertia) > 0;
        State newState;

        if (aboveThreshold) {
            newState = State.ABOVE_BEFORE_INERTIA;
            if (pastInertia) {
                newState = State.ABOVE;
            }
        } else {
            newState = State.BELOW_BEFORE_INERTIA;
            if (pastInertia) {
                newState = State.BELOW;
            }
        }

        MonitorEvent event = null;
        if (newState != state) {
            switch (newState) {
                case BELOW:
                    stateChangedAt = null;
                    event = new MonitorEvent<T>(
                            now,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.STOP,
                            value
                    );
                    break;
                case BELOW_BEFORE_INERTIA:
                case ABOVE_BEFORE_INERTIA:
                    stateChangedAt = now;
                    break;
                case ABOVE:
                    stateChangedAt = null;
                    event = new MonitorEvent<T>(
                            now,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.START,
                            value
                    );
                    break;
            }
        }
        return Optional.ofNullable(event);
    }

    public String getId() {
        return id;
    }

    protected abstract T value(SystemLoad systemLoad);

    protected abstract boolean isAboveThreshold(T value);

    protected abstract boolean isNearThreshold(T value);
}
