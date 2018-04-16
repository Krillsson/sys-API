package com.krillsson.sysapi.core.monitor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.krillsson.sysapi.core.query.QueryEvent;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MonitorManager implements Managed {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonitorManager.class);

    private final List<Monitor> activeMonitors = new ArrayList<>();
    private final List<MonitorEvent> events = new ArrayList<>();
    private final EventBus eventBus;

    public MonitorManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void addMonitor(Monitor monitor) {
        activeMonitors.add(monitor);
    }

    public void removeMonitor(Monitor monitor) {
        activeMonitors.remove(monitor);
    }

    @Subscribe
    public void onEvent(QueryEvent event) {
        for (Monitor activeMonitor : activeMonitors) {
            Optional<MonitorEvent> check = activeMonitor.check(event.load());
            check.ifPresent(events::add);
            if (!check.isPresent()) {
                LOGGER.debug("No event for {}", activeMonitor);
            } else {
                LOGGER.warn("Event: {}", check.get());
            }
        }

    }

    @Override
    public void start() throws Exception {
        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        eventBus.unregister(this);
    }
}
