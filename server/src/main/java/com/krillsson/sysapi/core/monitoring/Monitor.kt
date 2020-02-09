package com.krillsson.sysapi.core.monitoring

import com.google.common.annotations.VisibleForTesting
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.util.Clock
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

abstract class Monitor @VisibleForTesting protected constructor(private val clock: Clock) {
    private var stateChangedAt: OffsetDateTime? = null
    @get:VisibleForTesting
    var state = State.INSIDE
        private set
    private var eventId: UUID? = null

    enum class State {
        INSIDE, OUTSIDE_BEFORE_INERTIA, OUTSIDE, INSIDE_BEFORE_INERTIA
    }

    /**
     * Valid state changes
     *
     *
     * Inside -> Inside
     * no action
     *
     *
     * Inside -> Outside before inertia
     * (conditional: outside threshold)
     * save timestamp of state change
     *
     *
     * Outside before inertia -> Outside before inertia
     * no action
     *
     *
     * Outside before inertia -> inside
     * (conditional: inside threshold)
     * reset timestamp
     *
     *
     * Outside before inertia -> outside
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     *
     *
     * Outside -> outside
     * no action
     *
     *
     * Outside -> inside before inertia
     * (conditional: inside threshold)
     * save timestamp of state change
     *
     *
     * Inside before inertia -> inside
     * (conditional: now-timestamp older than inertia)
     * record event
     * reset timestamp (?)
     *
     *
     * Inside before inertia -> Inside before inertia
     * no action
     *
     *
     * Inside before inertia -> outside
     * reset timestamp
     *
     * @param systemLoad
     * @return
     */
    fun check(systemLoad: SystemLoad, input: MonitorInput): Optional<MonitorEvent> {
        val now = clock.now()
        val value = input.value(systemLoad)
        val outsideThreshold = input.isPastThreshold(value)
        val pastInertia = stateChangedAt != null && Duration.between(stateChangedAt,  /* and */now).compareTo(input.inertia) > 0
        var event: MonitorEvent? = null
        when (state) {
            State.INSIDE -> {
                if (outsideThreshold) { //Inside -> Outside before inertia
                    stateChangedAt = now
                    state = State.OUTSIDE_BEFORE_INERTIA
                    LOGGER.trace("{} went outside threshold of {} with {} at {}", input.id, input.threshold, value, now)
                } else {
                    LOGGER.trace("{} is still inside threshold: {} with {}", input.id, input.threshold, value)
                }
            }
            State.OUTSIDE_BEFORE_INERTIA -> {
                if (outsideThreshold) {
                    if (pastInertia) { //Outside before inertia -> outside
                        LOGGER.debug("{} have now been outside threshold of {} for more than {}, triggering event...", input.id, input.threshold, input.inertia)
                        state = State.OUTSIDE
                        stateChangedAt = null
                        eventId = UUID.randomUUID()
                        event = MonitorEvent(
                                eventId, input.id, now,
                                MonitorEvent.MonitorStatus.START,
                                input.type, input.threshold,
                                value
                        )
                    } else { //Outside before inertia -> Outside before inertia
                        LOGGER.trace("{} is still outside threshold of {} but inside grace period of {}", input.id, input.threshold, input.inertia)
                    }
                } else { //Outside before inertia -> inside
                    LOGGER.trace("{} went back inside threshold of {} inside grace period of {}", input.id, input.threshold, input.inertia)
                    stateChangedAt = null
                    state = State.INSIDE
                }
            }
            State.OUTSIDE -> {
                if (outsideThreshold) { //Outside -> outside
                    LOGGER.trace("{} is still outside threshold of {} at {}", input.id, input.threshold, value)
                } else { //Outside -> Inside before inertia
                    stateChangedAt = now
                    state = State.INSIDE_BEFORE_INERTIA
                    LOGGER.trace("{} went inside threshold of {} at {}", input.id, input.threshold, now)
                }
            }
            State.INSIDE_BEFORE_INERTIA -> {
                if (!outsideThreshold) {
                    if (pastInertia) { //Inside before inertia -> inside
                        LOGGER.debug("{} have now been inside threshold of {} for more than {}, triggering event...", input.id, input.threshold, input.inertia)
                        state = State.INSIDE
                        stateChangedAt = null
                        event = MonitorEvent(
                                eventId, input.id, now,
                                MonitorEvent.MonitorStatus.STOP,
                                input.type, input.threshold,
                                value
                        )
                    } else { //Inside before inertia -> Inside before inertia
                        LOGGER.trace("{} is still inside threshold of {} with {} but inside grace period of {}", input.id, input.threshold, value, input.inertia)
                    }
                } else { //Inside before inertia -> outside
                    LOGGER.trace("{} went back outside threshold of {} with {} inside grace period of {}", input.id, input.threshold, value, input.inertia)
                    stateChangedAt = null
                    state = State.OUTSIDE
                }
            }
        }
        return Optional.ofNullable(event)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Monitor::class.java)
    }

}