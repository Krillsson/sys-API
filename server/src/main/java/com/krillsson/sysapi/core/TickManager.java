package com.krillsson.sysapi.core;

import io.dropwizard.lifecycle.Managed;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickManager implements Managed {

    public interface TickListener {
        void onTick();
    }

    private final ScheduledExecutorService executorService;
    private final long measurementInterval;
    private final List<TickListener> listeners = new ArrayList<>();


    public TickManager(ScheduledExecutorService executorService, int measurementInterval) {
        this.executorService = executorService;
        this.measurementInterval = Duration.ofSeconds(measurementInterval).getSeconds();
    }

    public void register(TickListener listener) {
        listeners.add(listener);
    }

    public void unregister(TickListener tickListener) {
        listeners.remove(tickListener);
    }


    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(this::execute, 1, measurementInterval, TimeUnit.SECONDS);
    }

    private void execute() {
        for (TickListener listener : listeners) {
            listener.onTick();
        }
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }
}
