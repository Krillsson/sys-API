package com.krillsson.sysapi.core.monitor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.krillsson.sysapi.core.query.QueryEvent;
import com.krillsson.sysapi.persistence.LevelDbJacksonKeyValueStore;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class MonitorManager implements Managed {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonitorManager.class);

    private final Map<String, Monitor> activeMonitors = new HashMap<>();
    private final List<MonitorEvent> events = new ArrayList<>();
    private final EventBus eventBus;
    private final LevelDbJacksonKeyValueStore<com.krillsson.sysapi.dto.monitor.Monitor> persistentMonitors;

    public MonitorManager(EventBus eventBus, LevelDbJacksonKeyValueStore<com.krillsson.sysapi.dto.monitor.Monitor> persistentMonitors) {
        this.eventBus = eventBus;
        this.persistentMonitors = persistentMonitors;
    }

    public void addMonitor(Monitor monitor) {
        if (activeMonitors.containsKey(monitor.getId())) {
            LOGGER.debug("Updating monitor");
        }
        activeMonitors.put(monitor.getId(), monitor);
    }

    public void removeMonitor(String id) {
        activeMonitors.remove(id);
    }

    @Subscribe
    public void onEvent(QueryEvent event) {
        for (Monitor activeMonitor : activeMonitors.values()) {
            Optional<MonitorEvent> check = activeMonitor.check(event.load());
            check.ifPresent(events::add);
            if (!check.isPresent()) {
                LOGGER.trace("No event for {}", activeMonitor);
            } else {
                LOGGER.warn("Event: {}", check.get());
            }
        }
    }

    public List<MonitorEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void start() throws Exception {
        persistentMonitors.loadAll().forEach(e -> activeMonitors.put(e.key(), MonitorMapper.INSTANCE.map(e.value())));
        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        Map<String, com.krillsson.sysapi.dto.monitor.Monitor> map = activeMonitors.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> MonitorMapper.INSTANCE.map(e.getValue())));
        persistentMonitors.removeAll(map.keySet());
        persistentMonitors.putAll(map);
        persistentMonitors.close();
        eventBus.unregister(this);
    }
}
