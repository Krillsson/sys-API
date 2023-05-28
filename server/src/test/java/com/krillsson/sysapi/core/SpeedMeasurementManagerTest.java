package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpeedMeasurementManagerTest {

    static final String EN_0 = "en0";
    static final String EN_1 = "en1";
    SpeedMeasurementManager measurementManager;
    Clock clock;
    SpeedMeasurementManager.SpeedSource speedSource;
    SpeedMeasurementManager.SpeedSource secondSpeedSource;

    @Before
    public void setUp() throws Exception {
        speedSource = mock(SpeedMeasurementManager.SpeedSource.class);
        when(speedSource.getName()).thenReturn(EN_0);

        secondSpeedSource = mock(SpeedMeasurementManager.SpeedSource.class);
        when(secondSpeedSource.getName()).thenReturn(EN_1);

        clock = mock(Clock.class);

        measurementManager = new NetworkUploadDownloadRateMeasurementManager(clock);
    }

    @Test
    public void shouldMeasureSpeedForRegisteredSources() throws Exception {
        setupClockMock();
        when(speedSource.currentRead()).thenReturn(1000L, 2000L);
        when(speedSource.currentWrite()).thenReturn(2000L, 4000L);

        measurementManager.register(speedSource);

        //first run will only initialize
        measurementManager.run();

        // second run will give you a value
        measurementManager.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);

        assertNotNull(en0);
        assertEquals(200L, en0.get().readPerSeconds);
        assertEquals(400L, en0.get().writePerSeconds);
    }

    @Test
    public void speedSourceReturningZeroShouldNotCauseIssues() throws Exception {
        setupClockMock();
        when(speedSource.currentRead()).thenReturn(0L, 0L);
        when(speedSource.currentWrite()).thenReturn(0L, 0L);

        measurementManager.register(speedSource);

        //first run will only initialize
        measurementManager.run();

        // second run will give you a value
        measurementManager.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);

        assertTrue(en0.isPresent());
        assertEquals(0L, en0.get().readPerSeconds);
        assertEquals(0L, en0.get().writePerSeconds);
    }

    @Test
    public void multipleSources() throws Exception {
        setupClockMockForTwoSources();
        when(speedSource.currentRead()).thenReturn(1000L, 2000L);
        when(speedSource.currentWrite()).thenReturn(2000L, 4000L);

        when(secondSpeedSource.currentRead()).thenReturn(3000L, 12000L);
        when(secondSpeedSource.currentWrite()).thenReturn(8000L, 16000L);

        measurementManager.register(Arrays.asList(speedSource, secondSpeedSource));

        //first run will only initialize
        measurementManager.run();

        // second run will give you a value
        measurementManager.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);
        Optional<SpeedMeasurementManager.CurrentSpeed> en1 = measurementManager.getCurrentSpeedForName(EN_1);

        assertNotNull(en0);
        assertEquals(200L, en0.get().readPerSeconds);
        assertEquals(400L, en0.get().writePerSeconds);

        assertNotNull(en1);
        assertEquals(1800L, en1.get().readPerSeconds);
        assertEquals(1600L, en1.get().writePerSeconds);
    }

    @Test
    public void unregisterShouldQuitMeasuring() throws Exception {
        setupClockMock();

        when(speedSource.currentRead()).thenReturn(1000L, 2000L);
        when(speedSource.currentWrite()).thenReturn(2000L, 4000L);

        measurementManager.register(speedSource);
        measurementManager.unregister(speedSource);

        measurementManager.run();
        measurementManager.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0Optional = measurementManager.getCurrentSpeedForName(EN_0);
        assertFalse(en0Optional.isPresent());
    }

    void setupClockMock() {
        //clock.getZone().getRules().getOffset(now);
        ZoneId zoneId = ZoneId.systemDefault();
        when(clock.getZone()).thenReturn(zoneId);

        Instant first = Instant.now();                  // e.g. 12:00:00
        Instant second = first.plusSeconds(5);          // 12:00:05
        Instant thirdAndAfter = second.plusSeconds(5);  // 12:00:10

        when(clock.instant()).thenReturn(first, second, thirdAndAfter);
    }

    void setupClockMockForTwoSources() {
        //clock.getZone().getRules().getOffset(now);
        ZoneId zoneId = ZoneId.systemDefault();
        when(clock.getZone()).thenReturn(zoneId);

        Instant first = Instant.now();                  // e.g. 12:00:00
        Instant second = first.plusSeconds(5);          // 12:00:05
        Instant thirdAndAfter = second.plusSeconds(5);  // 12:00:10

        when(clock.instant()).thenReturn(first, first, second, second, thirdAndAfter, thirdAndAfter);
    }
}