package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.event.Event;
import com.krillsson.sysapi.core.domain.event.OngoingEvent;
import com.krillsson.sysapi.core.domain.event.PastEvent;
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig;
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
    static final int OUTSIDE = 1;
    static final int INSIDE = 0;
    TestableMonitor monitor;
    MonitorMechanism mechanism;
    Clock clock;
    SystemLoad systemLoad;
    MonitorMetricQueryEvent load;

    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        clock.useFixedClockAt(clock.now());
        monitor = new TestableMonitor();

        mechanism = new MonitorMechanism(clock);
        systemLoad = mock(SystemLoad.class);
        load = new MonitorMetricQueryEvent(systemLoad, Collections.emptyList());
    }

    @Test
    public void nothingIsWrong() {
        assertNull(mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load)));
        assertSame(mechanism.getState(), MonitorMechanism.State.INSIDE);
    }

    @Test
    public void insideToOutSideBeforeInertia() {
        monitor.value = OUTSIDE;

        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        assertNull(event);
        assertSame(mechanism.getState(), MonitorMechanism.State.OUTSIDE_BEFORE_INERTIA);
    }

    @Test
    public void outsideBeforeInertiaToOutSide() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        assertNull(event);

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        assertNotNull(event);

        assertEquals(event.getValue(), OUTSIDE, 0.0);
        assertEquals(event.getThreshold(), OUTSIDE, 0.0);
        assertEquals(event.getStartTime(), clock.now());
        assertTrue(event instanceof OngoingEvent);
    }

    @Test
    public void outsideToInsideBeforeInertia() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //going to inside before inertia
        monitor.value = INSIDE;
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        assertNull(event);
    }

    @Test
    public void insideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //going to inside before inertia
        monitor.value = INSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        assertEquals(monitorEvent.getValue(), INSIDE, 0.0);
        assertEquals(monitorEvent.getThreshold(), OUTSIDE, 0.0);
        assertEquals(monitorEvent.getStartTime(), clock.now());
        assertTrue(monitorEvent instanceof PastEvent);

    }

    @Test
    public void insideToOutSideAfterInertia() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));

        //going to outside
        Event event = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        assertNotNull(event);
        assertSame(mechanism.getState(), MonitorMechanism.State.OUTSIDE);

        //should have outside event

        assertEquals(monitor.id, event.getMonitorId());
        assertEquals(clock.now(), event.getStartTime());
        assertEquals(1, event.getThreshold(), 0.0);
        assertEquals(Monitor.Type.CPU_LOAD, event.getMonitorType());
    }

    @Test
    public void outsideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //time does not go past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(NOT_PAST_INERTIA));

        //going back to inside
        monitor.value = INSIDE;
        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        assertNull(monitorEvent);
    }

    @Test
    public void insideBeforeInertiaToOutside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //going to inside before inertia
        monitor.value = INSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        //time does not go past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(NOT_PAST_INERTIA));

        //going to outside again
        monitor.value = OUTSIDE;
        mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));

        Event monitorEvent = mechanism.check(monitor, monitor.config, monitor.selectValue(load), monitor.check(load));
        assertNull(monitorEvent);
    }

    private static class TestableMonitor extends Monitor {

        double value = INSIDE;
        UUID id = UUID.randomUUID();
        MonitorConfig config = new MonitorConfig("CPU", OUTSIDE, Duration.ofSeconds(INERTIA));

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
        public MonitorConfig getConfig() {
            return config;
        }

        @Override
        public double selectValue(@NotNull MonitorMetricQueryEvent load) {
            return value;
        }

        @Override
        public boolean isPastThreshold(double value) {
            return value == OUTSIDE;
        }
    }
}