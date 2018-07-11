package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.Clock;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;

import static com.krillsson.sysapi.core.monitoring.Monitor.MonitorType.CPU;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MonitorTest {

    TestableMonitor monitor;
    Clock clock;
    SystemLoad load;
    static final int OUTSIDE = 1;
    static final int INSIDE = 0;

    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        clock.useFixedClockAt(clock.now());

        monitor = new TestableMonitor("ID", Duration.ofSeconds(20), 1, clock);
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
    public void insideToOutSideAfterInertia() {
        monitor.value = OUTSIDE;

        monitor.check(load);

        clock.useFixedClockAt(clock.now().plusSeconds(30));

        Optional<MonitorEvent> event = monitor.check(load);
        assertTrue(event.isPresent());
        assertSame(monitor.getState(), Monitor.State.OUTSIDE);

        MonitorEvent monitorEvent = event.get();

        assertEquals( "ID", monitorEvent.getItemId());
        assertEquals(clock.now(), monitorEvent.getTime());
        assertEquals(1, monitorEvent.getThreshold(), 0.0);
        assertEquals(CPU, monitorEvent.getMonitorType());
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