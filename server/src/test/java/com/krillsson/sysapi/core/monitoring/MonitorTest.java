package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.util.TimeMachine;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static com.krillsson.sysapi.core.monitoring.Monitor.MonitorType.CPU;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class MonitorTest {

    TestableMonitor monitor;
    TimeMachine timeMachine;
    SystemLoad systemLoad;

    @Before
    public void setUp() throws Exception {
        timeMachine = new TimeMachine();
        monitor = new TestableMonitor("ID", Duration.ofSeconds(20), 20, timeMachine);
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

        protected TestableMonitor(String id, Duration inertia, double threshold, TimeMachine timeMachine) {
            super(id, inertia, threshold, timeMachine);
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