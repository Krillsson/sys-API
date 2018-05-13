package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.Duration.between;

public abstract class Monitor {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Monitor.class);
    private final String id;
    private final double threshold;
    //id to monitoring
    private final Duration inertia;
    private LocalDateTime stateChangedAt = null;
    private State state = State.INSIDE;

    enum State {
        INSIDE,
        PAST_BEFORE_INERTIA,
        PAST,
        INSIDE_BEFORE_INERTIA
    }

    protected enum MonitorType {
        CPU,
        CPU_TEMP,
        DRIVE,
        GPU,
        MEMORY,
        NETWORK_UP
    }

    protected Monitor(String id, Duration inertia, double threshold) {
        this.id = id;
        this.inertia = inertia;
        this.threshold = threshold;
    }

    /**
     * Valid state changes
     * <p>
     * Inside -> Inside
     * no action
     * <p>
     * Inside -> Past before inertia
     * (conditional: past threshold)
     * save timestamp of state change
     * <p>
     * Past before inertia -> Past before inertia
     * no action
     * <p>
     * Past before inertia -> inside
     * (conditional: inside threshold)
     * reset timestamp
     * <p>
     * Past before inertia -> past
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     * <p>
     * Past -> past
     * no action
     * <p>
     * Past -> inside before inertia
     * (conditional: inside threshold)
     * save timestamp of state change
     * <p>
     * Inside before inertia -> inside
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     * <p>
     * Inside before inertia -> Inside before inertia
     * no action
     * <p>
     * Inside before inertia -> past
     * reset timestamp
     *
     * @param systemLoad
     * @return
     */
    Optional<MonitorEvent> check(SystemLoad systemLoad) {

        LocalDateTime now = LocalDateTime.now();

        Double value = value(systemLoad);
        boolean pastThreshold = isPastThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(stateChangedAt, /* and */ now).compareTo(inertia) > 0;
        MonitorEvent event = null;

        if(state == State.INSIDE){
            if(pastThreshold){
                //Inside -> Past before inertia
                stateChangedAt = now;
                state = State.PAST_BEFORE_INERTIA;
                LOGGER.debug("{} went past threshold of {} with {} at {}", id(), threshold(), value, now);
            }
            else{
                LOGGER.debug("{} is still inside threshold: {} with {}", id(), threshold(), value);
            }
        }
        else if(state == State.PAST_BEFORE_INERTIA){
            if(pastThreshold){
                if(pastInertia){
                    //Past before inertia -> past
                    LOGGER.debug("{} have now been past threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.PAST;
                    stateChangedAt = null;
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.MonitorStatus.START,
                            type(), threshold(),
                            value
                    );
                }
                else{
                    //Past before inertia -> Past before inertia
                    LOGGER.debug("{} is still past threshold of {} but inside grace period of {}", id(), threshold(), inertia());
                }
            }
            else{
                //Past before inertia -> inside
                LOGGER.debug("{} went back inside threshold of {} inside grace period of {}", id(), threshold(), inertia());
                stateChangedAt = null;
                state = State.INSIDE;
            }
        }
        else if (state == State.PAST){
            if(pastThreshold){
                //Past -> past
                LOGGER.debug("{} is still past threshold of {} at {}", id(), threshold(), value);
            }else{
                //Past -> Inside before inertia
                stateChangedAt = now;
                state = State.INSIDE_BEFORE_INERTIA;
                LOGGER.debug("{} went inside threshold of {} at {}", id(), threshold(), now);
            }
        }
        else if(state == State.INSIDE_BEFORE_INERTIA){
            if(pastThreshold){
                if(pastInertia){
                    //Inside before inertia -> inside
                    LOGGER.debug("{} have now been inside threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.INSIDE;
                    stateChangedAt = null;
                    event = new MonitorEvent(
                            now,
                            id,
                            MonitorEvent.MonitorStatus.STOP,
                            type(), threshold(),
                            value
                    );
                }
                else{
                    //Inside before inertia -> Inside before inertia
                    LOGGER.debug("{} is still inside threshold of {} with {} but inside grace period of {}", id(), threshold(), value, inertia());
                }
            }
            else{
                //Inside before inertia -> past
                LOGGER.debug("{} went back past threshold of {} with {} inside grace period of {}", id(), threshold(), value, inertia());
                stateChangedAt = null;
                state = State.PAST;
            }
        }

        return Optional.ofNullable(event);

    }

    public String id() {
        return id;
    }

    protected abstract double value(SystemLoad systemLoad);

    protected abstract boolean isPastThreshold(double value);

    protected abstract MonitorType type();

    protected double threshold(){
        return threshold;
    }

    Duration inertia() {
        return inertia;
    }
}
