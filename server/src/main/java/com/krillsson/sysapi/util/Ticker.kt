package com.krillsson.sysapi.util;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ticker implements Managed {

    public interface TickListener {
        void onTick();
    }

    private final ScheduledExecutorService executorService;
    private final long measurementInterval;
    private final List<TickListener> listeners = new ArrayList<>();

    private final Logger LOGGER = LoggerFactory.getLogger(Ticker.class);

    public Ticker(ScheduledExecutorService executorService, int measurementInterval) {
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
            try {
                listener.onTick();
            } catch (Exception e) {
                LOGGER.error("Error while executing ticker", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }
}
