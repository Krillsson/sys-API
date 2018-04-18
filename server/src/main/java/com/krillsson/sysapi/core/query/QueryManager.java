package com.krillsson.sysapi.core.query;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import io.dropwizard.lifecycle.Managed;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;

public class QueryManager implements Managed {

    private final ScheduledExecutorService executorService;
    private final MetricsFactory provider;
    private final EventBus eventBus;
    private final HistoryConfiguration configuration;

    public QueryManager(ScheduledExecutorService executorService, HistoryConfiguration configuration, MetricsFactory provider, EventBus eventBus) {
        this.configuration = configuration;
        this.executorService = executorService;
        this.provider = provider;
        this.eventBus = eventBus;
    }

    private void query() {
        eventBus.post(new QueryEvent(new SystemLoad(
                provider.cpuMetrics().cpuLoad(),
                provider.networkMetrics().networkInterfaceLoads(),
                provider.driveMetrics().driveLoads(),
                provider.memoryMetrics().globalMemory(),
                provider.gpuMetrics().gpuLoads(),
                provider.motherboardMetrics().motherboardHealth()
        )));
    }

    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(
                this::query,
                0,
                configuration.getDuration(),
                configuration.getUnit()
        );
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }
}
