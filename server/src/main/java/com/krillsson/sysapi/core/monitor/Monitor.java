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
    private State state = State.BELOW;

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

    /**
     * Valid state changes
     * <p>
     * Below -> Below
     * no action
     * <p>
     * Below -> Above before inertia
     * (conditional: above threshold)
     * save timestamp of state change
     * <p>
     * Above before inertia -> Above before inertia
     * no action
     * <p>
     * Above before inertia -> below
     * (conditional: below threshold)
     * reset timestamp
     * <p>
     * Above before inertia -> above
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     * <p>
     * Above -> above
     * no action
     * <p>
     * Above -> below before inertia
     * (conditional: below threshold)
     * save timestamp of state change
     * <p>
     * Below before inertia -> below
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     * <p>
     * Below before inertia -> Below before inertia
     * no action
     * <p>
     * Below before inertia -> above
     * reset timestamp
     *
     * @param systemLoad
     * @return
     */
    Optional<MonitorEvent> check(SystemLoad systemLoad) {

        LocalDateTime now = LocalDateTime.now();

        Double value = value(systemLoad);
        boolean aboveThreshold = isAboveThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(stateChangedAt, /* and */ now).compareTo(inertia) > 0;
        MonitorEvent event = null;

        if(state == State.BELOW){
            if(aboveThreshold){
                //Below -> Above before inertia
                stateChangedAt = now;
                state = State.ABOVE_BEFORE_INERTIA;
                LOGGER.debug("{} went above threshold of {} with {} at {}", id(), threshold(), value, now);
            }
            else{
                LOGGER.debug("Still below threshold");
            }
        }
        else if(state == State.ABOVE_BEFORE_INERTIA){
            if(aboveThreshold){
                if(pastInertia){
                    //Above before inertia -> above
                    LOGGER.debug("{} have now been above threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.ABOVE;
                    stateChangedAt = null;
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.START,
                            threshold(), value
                    );
                }
                else{
                    //Above before inertia -> Above before inertia
                    LOGGER.debug("{} is still above threshold of {} but inside grace period of {}", id(), threshold(), inertia());
                }
            }
            else{
                //Above before inertia -> below
                LOGGER.debug("{} went back below threshold of {} inside grace period of {}", id(), threshold(), inertia());
                stateChangedAt = null;
                state = State.BELOW;
            }
        }
        else if (state == State.ABOVE){
            if(aboveThreshold){
                //Above -> above
                LOGGER.debug("{} is still above threshold", id());
            }else{
                //Above -> Below before inertia
                stateChangedAt = now;
                state = State.BELOW_BEFORE_INERTIA;
                LOGGER.debug("{} went below threshold of {} at {}", id(), threshold(), now);
            }
        }
        else if(state == State.BELOW_BEFORE_INERTIA){
            if(aboveThreshold){
                if(pastInertia){
                    //Below before inertia -> below
                    LOGGER.debug("{} have now been below threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.BELOW;
                    stateChangedAt = null;
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.Severity.CRITICAL,
                            MonitorEvent.Type.STOP,
                            threshold(), value
                    );
                }
                else{
                    //Below before inertia -> Below before inertia
                    LOGGER.debug("{} is still below threshold of {} but inside grace period of {}", id(), threshold(), inertia());
                }
            }
            else{
                //Below before inertia -> above
                LOGGER.debug("{} went back above threshold of {} inside grace period of {}", id(), threshold(), inertia());
                stateChangedAt = null;
                state = State.ABOVE;
            }
        }

        return Optional.ofNullable(event);

    }


    Optional<MonitorEvent> checkv1(SystemLoad systemLoad) {

        LocalDateTime now = LocalDateTime.now();

        Double value = value(systemLoad);
        boolean aboveThreshold = isAboveThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(stateChangedAt, /* and */ now).compareTo(inertia) > 0;
        State newState;

        if (aboveThreshold) {
            newState = State.ABOVE_BEFORE_INERTIA;
            if (pastInertia || state == State.ABOVE) {
                newState = State.ABOVE;
            }
        } else {
            newState = State.BELOW_BEFORE_INERTIA;
            if (pastInertia || state == State.BELOW) {
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
                    stateChangedAt = null;
                    break;
                case BELOW_BEFORE_INERTIA:
                    if (state == State.ABOVE) {
                        LOGGER.debug(
                                "Monitor {} went below the threshold. Current value: {} Threshold: {}",
                                id,
                                value,
                                threshold()
                        );
                        stateChangedAt = now;
                    }
                    break;
                case ABOVE_BEFORE_INERTIA:
                    if (state == State.BELOW) {
                        LOGGER.debug(
                                "Monitor {} went above the threshold. Current value: {} Threshold: {}",
                                id,
                                value,
                                threshold()
                        );
                        stateChangedAt = now;
                    }
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
                    stateChangedAt = null;
                    break;
            }
            state = newState;
        } else {
            LOGGER.debug("State still at {}", state);
        }
        return Optional.ofNullable(event);
    }

    public String id() {
        return id;
    }

    protected abstract double value(SystemLoad systemLoad);

    protected abstract double threshold();

    protected abstract boolean isAboveThreshold(double value);

    Duration inertia() {
        return inertia;
    }
}
