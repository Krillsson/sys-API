package com.krillsson.sysapi.core.history;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.query.QueryEvent;
import io.dropwizard.lifecycle.Managed;

import java.util.List;

public class HistoryManager implements Managed {
    private final History<SystemLoad> history;
    private final EventBus eventBus;
    private final HistoryConfiguration configuration;

    public HistoryManager(HistoryConfiguration configuration, EventBus eventBus) {
        this.configuration = configuration;
        this.eventBus = eventBus;
        this.history = new History<>();
    }

    @Subscribe
    public void onEvent(QueryEvent event)
    {
        history.record(event.load());
        history.purge(configuration.getPurging().getOlderThan(), configuration.getPurging().getUnit());
    }

    @Override
    public void start() throws Exception {
        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        eventBus.unregister(this);
    }

    public List<History.HistoryEntry<SystemLoad>> getHistory() {
        return history.get();
    }
}
