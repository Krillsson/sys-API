package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.Clock;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.krillsson.sysapi.core.monitoring.Monitor.MonitorType.CPU;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MonitorTest {

    public static final int INERTIA = 20;
    public static final int PAST_INERTIA = INERTIA + 1;
    public static final int NOT_PAST_INERTIA = INERTIA - 1;
    TestableMonitor monitor;
    Clock clock;
    SystemLoad load;
    static final int OUTSIDE = 1;
    static final int INSIDE = 0;

    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        clock.useFixedClockAt(clock.now());

        monitor = new TestableMonitor("ID", Duration.ofSeconds(20), OUTSIDE, clock);
        load = mock(SystemLoad.class);
    }

    @Test
    public void nothingIsWrong() {
        assertFalse(monitor.check(load).isPresent());
        assertSame(monitor.getState(), Monitor.State.INSIDE);
    }

    @Test
    public void insideToOutSideBeforeInertia() {
        monitor.value = OUTSIDE;

        Optional<MonitorEvent> event = monitor.check(load);
        assertFalse(event.isPresent());
        assertSame(monitor.getState(), Monitor.State.OUTSIDE_BEFORE_INERTIA);
    }

    @Test
    public void outsideBeforeInertiaToOutSide() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        Optional<MonitorEvent> event = monitor.check(load);
        assertFalse(event.isPresent());

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        event = monitor.check(load);

        assertTrue(event.isPresent());

        MonitorEvent monitorEvent = event.get();

        assertEquals(monitorEvent.getValue(), OUTSIDE, 0.0);
        assertEquals(monitorEvent.getThreshold(), OUTSIDE, 0.0);
        assertEquals(monitorEvent.getTime(), clock.now());
        assertEquals(monitorEvent.getMonitorStatus(), MonitorEvent.MonitorStatus.START);
    }

    @Test
    public void outsideToInsideBeforeInertia() {

        //going to outside before inertia
        monitor.value = OUTSIDE;
        monitor.check(load);
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        monitor.check(load);

        //going to inside before inertia
        monitor.value = INSIDE;
        Optional<MonitorEvent> event = monitor.check(load);

        assertFalse(event.isPresent());
    }

    @Test
    public void insideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        monitor.check(load);
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        monitor.check(load);

        //going to inside before inertia
        monitor.value = INSIDE;
        monitor.check(load);

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        Optional<MonitorEvent> monitorEvent = monitor.check(load);
        MonitorEvent event = monitorEvent.get();

        assertEquals(event.getValue(), INSIDE, 0.0);
        assertEquals(event.getThreshold(), OUTSIDE, 0.0);
        assertEquals(event.getTime(), clock.now());
        assertEquals(event.getMonitorStatus(), MonitorEvent.MonitorStatus.STOP);
    }

    @Test
    public void insideToOutSideAfterInertia() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        monitor.check(load);

        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));

        //going to outside
        Optional<MonitorEvent> event = monitor.check(load);
        assertTrue(event.isPresent());
        assertSame(monitor.getState(), Monitor.State.OUTSIDE);

        //should have outside event
        MonitorEvent monitorEvent = event.get();

        assertEquals( "ID", monitorEvent.getItemId());
        assertEquals(clock.now(), monitorEvent.getTime());
        assertEquals(1, monitorEvent.getThreshold(), 0.0);
        assertEquals(CPU, monitorEvent.getMonitorType());
    }

    @Test
    public void outsideBeforeInertiaToInside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        monitor.check(load);

        //time does not go past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(NOT_PAST_INERTIA));

        //going back to inside
        monitor.value = INSIDE;
        Optional<MonitorEvent> monitorEvent = monitor.check(load);

        assertFalse(monitorEvent.isPresent());
    }

    @Test
    public void insideBeforeInertiaToOutside() {
        //going to outside before inertia
        monitor.value = OUTSIDE;
        monitor.check(load);
        //time goes past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(PAST_INERTIA));
        //going to outside
        monitor.check(load);

        //going to inside before inertia
        monitor.value = INSIDE;
        monitor.check(load);

        //time does not go past inertia
        clock.useFixedClockAt(clock.now().plusSeconds(NOT_PAST_INERTIA));

        //going to outside again
        monitor.value = OUTSIDE;
        monitor.check(load);

        Optional<MonitorEvent> monitorEvent = monitor.check(load);
        assertFalse(monitorEvent.isPresent());
    }

    private static class TestableMonitor extends Monitor {

        double value = INSIDE;


        TestableMonitor(String id, Duration inertia, double threshold, Clock clock) {
            super(id, inertia, threshold, clock);
        }

        @Override
        protected double value(SystemLoad systemLoad) {
            return value;
        }

        @Override
        protected boolean isOutsideThreshold(double value) {
            return value == OUTSIDE;
        }

        @Override
        protected MonitorType type() {
            return CPU;
        }
    }
}