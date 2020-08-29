package com.krillsson.sysapi.core.history;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.config.HistoryPurgingConfiguration;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import org.junit.Before;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HistoryManagerTest {

    HistoryManager historyManager;
    History history;
    private EventBus eventBus;
    private HistoryPurgingConfiguration purgingConfiguration;

    @Before
    public void setUp() throws Exception {
        HistoryConfiguration historyConfiguration = mock(HistoryConfiguration.class);
        purgingConfiguration = mock(HistoryPurgingConfiguration.class);
        when(historyConfiguration.getPurging()).thenReturn(purgingConfiguration);
        history = mock(History.class);
        eventBus = mock(EventBus.class);
        historyManager = new HistoryManager(historyConfiguration, eventBus);
    }

    @Test
    public void delegatesToHistoryOnAnEvent() {
        when(purgingConfiguration.getOlderThan()).thenReturn(1);
        when(purgingConfiguration.getUnit()).thenReturn(ChronoUnit.HOURS);

        historyManager.onEvent(new HistoryMetricQueryEvent(mock(SystemLoad.class)));

        verify(history).record(any());
        verify(history).purge(1, ChronoUnit.HOURS);
    }

    @Test
    public void startStopRegistersToEventBus() {

        historyManager.start();
        historyManager.stop();

        verify(eventBus).register(historyManager);
        verify(eventBus).unregister(historyManager);
    }
}