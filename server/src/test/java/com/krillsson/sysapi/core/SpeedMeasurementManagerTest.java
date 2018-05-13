package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class SpeedMeasurementManagerTest {

    static final String EN_0 = "en0";
    static final String EN_1 = "en1";
    SpeedMeasurementManager measurementManager;
    ScheduledExecutorService executorService;
    Clock clock;
    SpeedMeasurementManager.SpeedSource speedSource;
    SpeedMeasurementManager.SpeedSource secondSpeedSource;

    @Before
    public void setUp() throws Exception {
        speedSource = mock(SpeedMeasurementManager.SpeedSource.class);
        when(speedSource.getName()).thenReturn(EN_0);

        secondSpeedSource = mock(SpeedMeasurementManager.SpeedSource.class);
        when(secondSpeedSource.getName()).thenReturn(EN_1);

        executorService = mock(ScheduledExecutorService.class);
        clock = mock(Clock.class);

        measurementManager = new SpeedMeasurementManager(executorService, clock, 5);
    }

    @Test
    public void shouldMeasureSpeedForRegisteredSources() throws Exception {
        setupClockMock();
        when(speedSource.getCurrentRead()).thenReturn(1000L, 2000L);
        when(speedSource.getCurrentWrite()).thenReturn(2000L, 4000L);

        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(executorService.scheduleAtFixedRate(
                argumentCaptor.capture(),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenReturn(null);

        measurementManager.register(speedSource);
        measurementManager.start();

        Runnable value = argumentCaptor.getValue();

        //first run will only initialize
        value.run();

        // second run will give you a value
        value.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);

        assertNotNull(en0);
        assertEquals(200L, en0.get().getReadPerSeconds());
        assertEquals(400L, en0.get().getWritePerSeconds());
    }

    @Test
    public void speedSourceReturningZeroShouldNotCauseIssues() throws Exception {
        setupClockMock();
        when(speedSource.getCurrentRead()).thenReturn(0L, 0L);
        when(speedSource.getCurrentWrite()).thenReturn(0L, 0L);

        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(executorService.scheduleAtFixedRate(
                argumentCaptor.capture(),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenReturn(null);

        measurementManager.register(speedSource);
        measurementManager.start();

        Runnable value = argumentCaptor.getValue();

        //first run will only initialize
        value.run();

        // second run will give you a value
        value.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);

        assertTrue(en0.isPresent());
        assertEquals(0L, en0.get().getReadPerSeconds());
        assertEquals(0L, en0.get().getWritePerSeconds());
    }

    @Test
    public void multipleSources() throws Exception {
        setupClockMockForTwoSources();
        when(speedSource.getCurrentRead()).thenReturn(1000L, 2000L);
        when(speedSource.getCurrentWrite()).thenReturn(2000L, 4000L);

        when(secondSpeedSource.getCurrentRead()).thenReturn(3000L, 12000L);
        when(secondSpeedSource.getCurrentWrite()).thenReturn(8000L, 16000L);

        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(executorService.scheduleAtFixedRate(
                argumentCaptor.capture(),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenReturn(null);

        measurementManager.register(Arrays.asList(speedSource, secondSpeedSource));
        measurementManager.start();

        Runnable value = argumentCaptor.getValue();

        //first run will only initialize
        value.run();

        // second run will give you a value
        value.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0 = measurementManager.getCurrentSpeedForName(EN_0);
        Optional<SpeedMeasurementManager.CurrentSpeed> en1 = measurementManager.getCurrentSpeedForName(EN_1);

        assertNotNull(en0);
        assertEquals(200L, en0.get().getReadPerSeconds());
        assertEquals(400L, en0.get().getWritePerSeconds());

        assertNotNull(en1);
        assertEquals(1800L, en1.get().getReadPerSeconds());
        assertEquals(1600L, en1.get().getWritePerSeconds());
    }

    @Test
    public void unregisterShouldQuitMeasuring() throws Exception {
        setupClockMock();

        when(speedSource.getCurrentRead()).thenReturn(1000L, 2000L);
        when(speedSource.getCurrentWrite()).thenReturn(2000L, 4000L);

        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(executorService.scheduleAtFixedRate(
                argumentCaptor.capture(),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenReturn(null);

        measurementManager.register(speedSource);
        measurementManager.start();
        measurementManager.unregister(speedSource);

        Runnable value = argumentCaptor.getValue();

        value.run();
        value.run();

        Optional<SpeedMeasurementManager.CurrentSpeed> en0Optional = measurementManager.getCurrentSpeedForName(EN_0);

        SpeedMeasurementManager.CurrentSpeed en0 = en0Optional.get();
        assertEquals(en0.getReadPerSeconds(), 0);
        assertEquals(en0.getWritePerSeconds(), 0);
        verify(speedSource, never()).getCurrentRead();
        verify(speedSource, never()).getCurrentWrite();
    }

    @Test
    public void stoppingManagerShutsDownExecutorService() throws Exception {
        when(executorService.scheduleAtFixedRate(
                any(Runnable.class),
                anyLong(),
                anyLong(),
                any(TimeUnit.class)
        )).thenReturn(null);

        measurementManager.start();
        measurementManager.stop();

        verify(executorService).shutdownNow();
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