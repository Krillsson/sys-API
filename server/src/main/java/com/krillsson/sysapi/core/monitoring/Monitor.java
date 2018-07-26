package com.krillsson.sysapi.core.monitoring;

import com.google.common.annotations.VisibleForTesting;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.Clock;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.Duration.between;

public abstract class Monitor {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Monitor.class);
    private String id;
    private final double threshold;
    //id to monitoring
    private final Duration inertia;
    private final Clock clock;
    private LocalDateTime stateChangedAt = null;
    private State state = State.INSIDE;
    private UUID eventId;

    enum State {
        INSIDE,
        OUTSIDE_BEFORE_INERTIA,
        OUTSIDE,
        INSIDE_BEFORE_INERTIA;
    }


    protected enum MonitorType {
        CPU,
        CPU_TEMP,
        DRIVE,
        GPU,
        MEMORY,
        NETWORK_UP;
    }
    protected Monitor(String id, Duration inertia, double threshold) {
        this(id, inertia, threshold, new Clock());
    }

    @VisibleForTesting
    protected Monitor(String id, Duration inertia, double threshold, Clock clock){
        this.id = id;
        this.inertia = inertia;
        this.threshold = threshold;
        this.clock = clock;
    }

    /**
     * Valid state changes
     * <p>
     * Inside -> Inside
     * no action
     * <p>
     * Inside -> Outside before inertia
     * (conditional: outside threshold)
     * save timestamp of state change
     * <p>
     * Outside before inertia -> Outside before inertia
     * no action
     * <p>
     * Outside before inertia -> inside
     * (conditional: inside threshold)
     * reset timestamp
     * <p>
     * Outside before inertia -> outside
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     * <p>
     * Outside -> outside
     * no action
     * <p>
     * Outside -> inside before inertia
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
     * Inside before inertia -> outside
     * reset timestamp
     *
     * @param systemLoad
     * @return
     */
    Optional<MonitorEvent> check(SystemLoad systemLoad) {

        LocalDateTime now = clock.now();

        Double value = value(systemLoad);
        boolean outsideThreshold = isOutsideThreshold(value);
        boolean pastInertia = stateChangedAt != null && between(stateChangedAt, /* and */ now).compareTo(inertia) > 0;
        MonitorEvent event = null;

        if(state == State.INSIDE){
            if(outsideThreshold){
                //Inside -> Outside before inertia
                stateChangedAt = now;
                state = State.OUTSIDE_BEFORE_INERTIA;
                LOGGER.trace("{} went outside threshold of {} with {} at {}", id(), threshold(), value, now);
            }
            else{
                LOGGER.trace("{} is still inside threshold: {} with {}", id(), threshold(), value);
            }
        }
        else if(state == State.OUTSIDE_BEFORE_INERTIA){
            if(outsideThreshold){
                if(pastInertia){
                    //Outside before inertia -> outside
                    LOGGER.debug("{} have now been outside threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.OUTSIDE;
                    stateChangedAt = null;
                    eventId = UUID.randomUUID();
                    event = new MonitorEvent(
                            eventId, id, now,
                            MonitorEvent.MonitorStatus.START,
                            type(), threshold(),
                            value
                    );
                }
                else{
                    //Outside before inertia -> Outside before inertia
                    LOGGER.trace("{} is still outside threshold of {} but inside grace period of {}", id(), threshold(), inertia());
                }
            }
            else{
                //Outside before inertia -> inside
                LOGGER.trace("{} went back inside threshold of {} inside grace period of {}", id(), threshold(), inertia());
                stateChangedAt = null;
                state = State.INSIDE;
            }
        }
        else if (state == State.OUTSIDE){
            if(outsideThreshold){
                //Outside -> outside
                LOGGER.trace("{} is still outside threshold of {} at {}", id(), threshold(), value);
            }else{
                //Outside -> Inside before inertia
                stateChangedAt = now;
                state = State.INSIDE_BEFORE_INERTIA;
                LOGGER.trace("{} went inside threshold of {} at {}", id(), threshold(), now);
            }
        }
        else if(state == State.INSIDE_BEFORE_INERTIA){
            if(!outsideThreshold){
                if(pastInertia){
                    //Inside before inertia -> inside
                    LOGGER.debug("{} have now been inside threshold of {} for more than {}, triggering event...", id(), threshold(), inertia());
                    state = State.INSIDE;
                    stateChangedAt = null;
                    event = new MonitorEvent(
                            eventId, id, now,
                            MonitorEvent.MonitorStatus.STOP,
                            type(), threshold(),
                            value
                    );
                }
                else{
                    //Inside before inertia -> Inside before inertia
                    LOGGER.trace("{} is still inside threshold of {} with {} but inside grace period of {}", id(), threshold(), value, inertia());
                }
            }
            else{
                //Inside before inertia -> outside
                LOGGER.trace("{} went back outside threshold of {} with {} inside grace period of {}", id(), threshold(), value, inertia());
                stateChangedAt = null;
                state = State.OUTSIDE;
            }
        }

        return Optional.ofNullable(event);

    }

    public String id() {
        return id;
    }

    protected abstract double value(SystemLoad systemLoad);

    protected abstract boolean isOutsideThreshold(double value);

    protected abstract MonitorType type();

    protected double threshold(){
        return threshold;
    }

    Duration inertia() {
        return inertia;
    }

    public void setId(String id) {
        this.id = id;
    }

    @VisibleForTesting
    State getState() {
        return state;
    }
}
