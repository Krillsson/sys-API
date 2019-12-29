package com.krillsson.sysapi.core.monitoring;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.krillsson.sysapi.core.metrics.Metrics;
import com.krillsson.sysapi.core.monitoring.monitors.*;
import com.krillsson.sysapi.persistence.JsonFile;
import io.dropwizard.lifecycle.Managed;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class MonitorManager implements Managed {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonitorManager.class);

    private final Map<String, Monitor> activeMonitors = new HashMap<>();
    private final List<MonitorEvent> events = new ArrayList<>();
    private final EventBus eventBus;
    private final JsonFile<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>> persistentMonitors;
    private final Metrics provider;

    public MonitorManager(EventBus eventBus, JsonFile<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>> persistentMonitors, Metrics provider) {
        this.eventBus = eventBus;
        this.persistentMonitors = persistentMonitors;
        this.provider = provider;
    }

    public String addMonitor(Monitor monitor) {
        if (monitor.id() == null) {
            monitor.setId(UUID.randomUUID().toString());
        }
        if (activeMonitors.containsKey(monitor.id())) {
            LOGGER.debug("Updating monitoring");
        }
        activeMonitors.put(monitor.id(), monitor);
        persist();

        return monitor.id();
    }

    @Subscribe
    public void onEvent(MonitorMetricQueryEvent event) {
        for (Monitor activeMonitor : activeMonitors.values()) {
            Optional<MonitorEvent> check = activeMonitor.check(event.load());
            check.ifPresent(e -> {

                LOGGER.warn("Event: {}", e);

                if (e.getMonitorStatus() == MonitorEvent.MonitorStatus.STOP && events.stream()
                        .noneMatch(me -> me.getId().equals(e.getId()))) {
                    LOGGER.warn("Received STOP event for explicitly removed event, ignoring...");
                } else {
                    events.add(e);
                }
            });
        }
    }

    public boolean validate(Monitor monitor) {
        return monitor.value(provider.consolidatedMetrics()) != -1.0;
    }

    @Nullable
    public String createAndAdd(int inertiaInSeconds, MonitorType type, double threshold) {
        Monitor monitor = create(type, threshold, Duration.ofSeconds(inertiaInSeconds));
        if(validate(monitor)){
            addMonitor(monitor);
            return monitor.id();
        }
        return null;
    }

    public List<Monitor> monitors() {
        return Collections.unmodifiableList(new ArrayList<>(activeMonitors.values()));
    }

    public List<MonitorEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void start() throws Exception {
        persistentMonitors.getPersistedData(false, value -> {
            value.entrySet().stream().forEach(e -> {
                LOGGER.debug("Registering monitoring ID: {} threshold: {}", e.getValue(), e.getValue().getThreshold());
                activeMonitors.put(e.getKey(), MonitorMapper.INSTANCE.map(e.getValue()));
            });
            //passed false as parameter so this won't do anything
            return null;
        });
        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        persist();
        eventBus.unregister(this);
    }

    private void persist() {
        persistentMonitors.getPersistedData(true, persistedMap -> {
            activeMonitors.forEach((key, value) -> persistedMap.put(key, MonitorMapper.INSTANCE.map(value)));
            return persistedMap;
        });
    }

    public boolean removeEvents(String id) {
        List<MonitorEvent> toBeRemoved = new ArrayList<>();
        for (MonitorEvent event : events) {
            if (event.getId().toString().equalsIgnoreCase(id)) {
                toBeRemoved.add(event);
            }
        }
        LOGGER.debug("Removed {} events with ID {}", toBeRemoved.size(), id);
        return events.removeAll(toBeRemoved);
    }

    public boolean remove(String id) {
        boolean status = activeMonitors.remove(id) != null;
        if (status) {
            persist();
        }
        return status;
    }

    public Optional<Monitor> monitorById(String id) {
        return Optional.ofNullable(activeMonitors.get(id));
    }

    public Optional<List<MonitorEvent>> eventsForMonitorWithId(String id) {
        if (activeMonitors.get(id) != null) {
            return Optional.of(events.stream()
                    .filter(event -> event.getMonitorId().equalsIgnoreCase(id))
                    .collect(Collectors.toList()));
        } else {
            return Optional.empty();
        }
    }

    com.krillsson.sysapi.core.monitoring.Monitor create(MonitorType type, double threshold, Duration durationSeconds) {
        String id = UUID.randomUUID().toString();
        switch (type) {
            case CPU_LOAD:
                return new CpuMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            case CPU_TEMP:
                return new CpuTemperatureMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            case DRIVE_SPACE:
                return new DriveMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            case GPU_LOAD:
                return new GpuMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            case MEMORY_SPACE:
                return new MemoryMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            case NETWORK_UP:
                return new NetworkUpMonitor(
                        id,
                        durationSeconds,
                        threshold
                );
            default:
                throw new IllegalArgumentException("Not supported");
        }
    }
}
