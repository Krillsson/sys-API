package com.krillsson.sysapi.core.history;

import io.dropwizard.lifecycle.Managed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HistoryManager implements Managed {
    private final Map<Class, History> histories;
    private final ScheduledExecutorService executorService;

    public HistoryManager(ScheduledExecutorService executorService) {
        this.histories = new HashMap<>();
        this.executorService = executorService;
    }

    public <T> void insert(Class clazz, History<T> history) {
        histories.put(clazz, history);
    }

    //TODO: not so pretty with the type system
    public <T> Map<LocalDateTime, T> get(Class clazz) {
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
        executorService.scheduleAtFixedRate(this::executeRecording, 1, 1, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(this::executePurging, 1, 12, TimeUnit.HOURS);
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
        histories.clear();
    }
}
