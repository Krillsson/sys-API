package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

public class MonitorManager implements Managed {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonitorManager.class);


    private final ScheduledExecutorService executorService;
    private final List<Monitor> activeMonitors = new ArrayList<>();
    private final List<MonitorEvent> events = new ArrayList<>();
    private final HistoryConfiguration configuration;
    private final MetricsHistoryManager metricsHistoryManager;

    public MonitorManager(ScheduledExecutorService executorService, HistoryConfiguration configuration, MetricsHistoryManager metricsHistoryManager) {
        this.executorService = executorService;
        this.configuration = configuration;
        this.metricsHistoryManager = metricsHistoryManager;
    }

    public void addMonitor(Monitor monitor) {
        activeMonitors.add(monitor);
    }

    public void removeMonitor(Monitor monitor){
        activeMonitors.remove(monitor);
    }


    private void executeMonitorRun() {
        for (Monitor activeMonitor : activeMonitors) {
            Optional<MonitorEvent> check = activeMonitor.check(metricsHistoryManager.systemLoadHistory());
            check.ifPresent(events::add);
            if(!check.isPresent()){
                LOGGER.debug("No event for {}", activeMonitor);
            }
            else{
                LOGGER.warn("Event: {}", check.get());
            }
        }
    }


    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(
                this::executeMonitorRun,
                configuration.getDuration(),
                configuration.getDuration(),
                configuration.getUnit()
        );
    }

    @Override
    public void stop() throws Exception {

    }
}
