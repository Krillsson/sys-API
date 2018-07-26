package com.krillsson.sysapi.core.query;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class MetricQueryManager<T extends MetricQueryEvent> implements Managed {

    private final ScheduledExecutorService executorService;
    private final MetricsFactory provider;
    private final EventBus eventBus;
    private final long period;
    private final TimeUnit unit;

    public MetricQueryManager(ScheduledExecutorService executorService, long period, TimeUnit unit, MetricsFactory provider, EventBus eventBus) {
        this.executorService = executorService;
        this.provider = provider;
        this.eventBus = eventBus;
        this.period = period;
        this.unit = unit;
    }

    private void query() {
        eventBus.post(event(provider.consolidatedMetrics()));
    }

    protected abstract T event(SystemLoad load);

    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(
                this::query,
                period,
                period,
                unit
        );
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }
}
