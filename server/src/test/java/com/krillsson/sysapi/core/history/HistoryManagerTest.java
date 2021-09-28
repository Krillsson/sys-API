package com.krillsson.sysapi.core.history;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.config.HistoryPurgingConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class HistoryManagerTest {

    HistoryManager historyManager;
    HistoryRepository history;
    private EventBus eventBus;
    private HistoryPurgingConfiguration purgingConfiguration;

    @Before
    public void setUp() throws Exception {
        HistoryConfiguration historyConfiguration = mock(HistoryConfiguration.class);
        purgingConfiguration = mock(HistoryPurgingConfiguration.class);
        when(historyConfiguration.getPurging()).thenReturn(purgingConfiguration);
        history = mock(HistoryRepository.class);
        eventBus = mock(EventBus.class);
        historyManager = new HistoryManager(historyConfiguration, eventBus, history);
    }

    @Test
    public void startStopRegistersToEventBus() {

        historyManager.start();
        historyManager.stop();

        verify(eventBus).register(historyManager);
        verify(eventBus).unregister(historyManager);
    }
}