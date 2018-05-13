package com.krillsson.sysapi.core.monitoring;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
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
    private final MetricsFactory provider;

    public MonitorManager(EventBus eventBus, LevelDbJacksonKeyValueStore<com.krillsson.sysapi.dto.monitor.Monitor> persistentMonitors, MetricsFactory provider) {
        this.eventBus = eventBus;
        this.persistentMonitors = persistentMonitors;
        this.provider = provider;
    }

    public void addMonitor(Monitor monitor) {
        if (activeMonitors.containsKey(monitor.id())) {
            LOGGER.debug("Updating monitoring");
        }
        activeMonitors.put(monitor.id(), monitor);
        persist();
    }

    @Subscribe
    public void onEvent(MonitorMetricQueryEvent event) {
        for (Monitor activeMonitor : activeMonitors.values()) {
            Optional<MonitorEvent> check = activeMonitor.check(event.load());
            check.ifPresent(events::add);
            check.ifPresent(e -> LOGGER.warn("Event: {}", check.get()));
        }
    }

    public boolean validate(Monitor monitor){
        return monitor.value(provider.consolidatedMetrics()) != -1.0;
    }

    public List<Monitor> monitors(){
        return Collections.unmodifiableList(new ArrayList<>(activeMonitors.values()));
    }

    public List<MonitorEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void start() throws Exception {
        persistentMonitors.loadAll().forEach(e -> {
            LOGGER.debug("Registering monitoring ID: {} threshold: {}", e.key(), e.value().getThreshold());
            activeMonitors.put(e.key(), MonitorMapper.INSTANCE.map(e.value()));
        });
        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        persist();
        persistentMonitors.close();
        eventBus.unregister(this);
    }

    private void persist() {
        Map<String, com.krillsson.sysapi.dto.monitor.Monitor> map = activeMonitors.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> MonitorMapper.INSTANCE.map(e.getValue())));
        persistentMonitors.removeAll(map.keySet());
        persistentMonitors.putAll(map);
    }

    public boolean remove(String id) {
        boolean status = activeMonitors.remove(id) != null;
        if(status){
            persist();
        }
        return status;
    }
}
