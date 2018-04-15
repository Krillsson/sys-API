package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.config.HistoryConfiguration;
import io.dropwizard.lifecycle.Managed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class HistoryManager implements Managed {
    private final Map<Class, History> histories;
    private final ScheduledExecutorService executorService;
    private final HistoryConfiguration configuration;

    public HistoryManager(ScheduledExecutorService executorService, HistoryConfiguration configuration) {
        this.configuration = configuration;
        this.histories = new HashMap<>();
        this.executorService = executorService;
    }

    public <T> void insert(Class clazz, History<T> history) {
        histories.put(clazz, history);
    }

    //TODO: not so pretty with the type system
    public <T> List<History.HistoryEntry<T>> get(Class clazz) {
        return histories.get(clazz).get();
    }

    private void executeRecording() {
        for (History history : histories.values()) {
            history.record();
        }
    }

    private void executePurging() {
        for (History history : histories.values()) {
            history.purge();
        }
    }

    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(
                this::executeRecording,
                0,
                configuration.getDuration(),
                configuration.getUnit()
        );
        executorService.scheduleWithFixedDelay(
                this::executePurging,
                configuration.getPurging().getPurgeEvery(),
                configuration.getPurging().getPurgeEvery(),
                configuration.getPurging().getPurgeEveryUnit()
        );
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
        histories.clear();
    }
}
