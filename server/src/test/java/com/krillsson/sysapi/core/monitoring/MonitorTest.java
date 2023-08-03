package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.event.Event;
import com.krillsson.sysapi.core.domain.event.OngoingEvent;
import com.krillsson.sysapi.core.domain.event.PastEvent;
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig;
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.Clock;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MonitorTest {

    public static final int INERTIA = 20;
    public static final int PAST_INERTIA = INERTIA + 1;
    public static final int NOT_PAST_INERTIA = INERTIA - 1;
    static final MonitoredValue.NumericalValue OUTSIDE = new MonitoredValue.NumericalValue(1);
    static final MonitoredValue.NumericalValue INSIDE = new MonitoredValue.NumericalValue(0);
    TestableMonitor monitor;
    MonitorMechanism mechanism;
    Clock clock;
    SystemLoad systemLoad;
    MetricQueryEvent load;

    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        clock.useFixedClockAt(clock.offsetDateTime());
        monitor = new TestableMonitor();

        mechanism = new MonitorMechanism(clock);
        systemLoad = mock(SystemLoad.class);
        load = new MetricQueryEvent(systemLoad, Collections.emptyList());
    }

    @Test
    public void nothingIsWrong() {
        assertNull(mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load))));
        assertSame(mechanism.getState(), MonitorMechanism.State.INSIDE);
    }

    @Test
    public void insideToOutSideBeforeInertia() {
        monitor.value = OUTSIDE;

        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        assertNull(event);
        assertSame(mechanism.getState(), MonitorMechanism.State.OUTSIDE_BEFORE_INERTIA);
    }

    @Test
    public void outsideBeforeInertiaToOutSide() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        assertNull(event);

        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));
        //going to outside
        event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        assertNotNull(event);

        assertEquals(event.getValue(), OUTSIDE);
        assertEquals(event.getThreshold(), OUTSIDE);
        assertEquals(event.getStartTime(), clock.offsetDateTime());
        assertTrue(event instanceof OngoingEvent);
    }

    @Test
    public void outsideToInsideBeforeInertia() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //going to inside before inertia
        monitor.value = INSIDE;
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        assertNull(event);
    }

    @Test
    public void insideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //going to inside before inertia
        monitor.value = INSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));
        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        assertEquals(monitorEvent.getValue(), INSIDE);
        assertEquals(monitorEvent.getThreshold(), OUTSIDE);
        assertEquals(monitorEvent.getStartTime(), clock.offsetDateTime());
        assertTrue(monitorEvent instanceof PastEvent);

    }

    @Test
    public void insideToOutSideAfterInertia() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));

        //going to outside
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        assertNotNull(event);
        assertSame(mechanism.getState(), MonitorMechanism.State.OUTSIDE);

        //should have outside event

        assertEquals(monitor.id, event.getMonitorId());
        assertEquals(clock.offsetDateTime(), event.getStartTime());
        assertEquals(1, event.getThreshold());
        assertEquals(Monitor.Type.CPU_LOAD, event.getMonitorType());
    }

    @Test
    public void outsideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //time does not go past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(NOT_PAST_INERTIA));

        //going back to inside
        monitor.value = INSIDE;
        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        assertNull(monitorEvent);
    }

    @Test
    public void insideBeforeInertiaToOutside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        //time goes past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //going to inside before inertia
        monitor.value = INSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        //time does not go past inertia
        clock.useFixedClockAt(clock.offsetDateTime().plusSeconds(NOT_PAST_INERTIA));

        //going to outside again
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));

        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.isPastThreshold(monitor.selectValue(load)));
        assertNull(monitorEvent);
    }

    private static class TestableMonitor extends Monitor {

        MonitoredValue value = INSIDE;
        UUID id = UUID.randomUUID();
        MonitorConfig<MonitoredValue> config = new MonitorConfig<MonitoredValue>("CPU", OUTSIDE, Duration.ofSeconds(INERTIA));

        public TestableMonitor() {
        }

        @NotNull
        @Override
        public UUID getId() {
            return id;
        }

        @NotNull
        @Override
        public Type getType() {
            return Type.CPU_LOAD;
        }

        @NotNull
        @Override
        public MonitorConfig<MonitoredValue> getConfig() {
            return config;
        }

        @Override
        public MonitoredValue selectValue(@NotNull MetricQueryEvent load) {
            return value;
        }

        @Override
        public boolean isPastThreshold(MonitoredValue value) {
            return value == OUTSIDE;
        }
    }
}