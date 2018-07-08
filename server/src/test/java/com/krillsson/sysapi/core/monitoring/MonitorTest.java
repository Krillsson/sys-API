package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.Clock;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static com.krillsson.sysapi.core.monitoring.Monitor.MonitorType.CPU;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class MonitorTest {

    TestableMonitor monitor;
    Clock clock;
    SystemLoad systemLoad;

    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        monitor = new TestableMonitor("ID", Duration.ofSeconds(20), 20, clock);
        systemLoad = mock(SystemLoad.class);
    }

    @Test
    public void nothingIsWrong() {
        assertFalse(monitor.check(systemLoad).isPresent());
    }

    private static class TestableMonitor extends Monitor {

        double value = INSIDE;
        static final int OUTSIDE = 1;
        static final int INSIDE = 0;

        protected TestableMonitor(String id, Duration inertia, double threshold, Clock clock) {
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