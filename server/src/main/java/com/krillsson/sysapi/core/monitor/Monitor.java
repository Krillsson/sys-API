package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.Duration.between;

public abstract class Monitor {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Monitor.class);
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

    Monitor(String id, Duration inertia) {
        this.id = id;
        this.inertia = inertia;
    }

    Optional<MonitorEvent> check(SystemLoad systemLoad) {

        LocalDateTime now = LocalDateTime.now();

        Double value = value(systemLoad);
        boolean aboveThreshold = isAboveThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(stateChangedAt, /* and */ now).compareTo(inertia) > 0;
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
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.STOP,
                            threshold(), value
                    );
                    break;
                case BELOW_BEFORE_INERTIA:
                case ABOVE_BEFORE_INERTIA:
                    LOGGER.debug(
                            "Monitor {} is {} the threshold. Current value: {} Threshold: {}",
                            id,
                            aboveThreshold ? "above" : "below",
                            value,
                            threshold()
                    );
                    stateChangedAt = now;
                    break;
                case ABOVE:
                    LOGGER.warn(
                            "Monitor {} have been above the threshold for more than {} Current value: {} Threshold: {}",
                            id,
                            inertia.toString(),
                            value,
                            threshold()
                    );
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.START,
                            threshold(), value
                    );
                    break;
            }
            state = newState;
        }
        return Optional.ofNullable(event);
    }

    public String getId() {
        return id;
    }

    protected abstract double value(SystemLoad systemLoad);

    protected abstract double threshold();

    protected abstract boolean isAboveThreshold(double value);

    Duration inertia() {
        return inertia;
    }
}
