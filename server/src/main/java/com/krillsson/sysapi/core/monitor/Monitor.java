package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.history.History;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Monitor<T, R> {
    //id to monitor
    private final Duration inertia;
    private final String id;
    private boolean isAboveThreshold;

    public Monitor(String id, Duration inertia) {
        this.id = id;
        this.inertia = inertia;
    }

    Optional<MonitorEvent> check(List<History.HistoryEntry<SystemLoad>> systemHistory) {

        //TODO this isn't inertia
        //TODO inertia: if we detect a raised value. It has to be rasied for x duration before we warn.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oldestPossible = now.minus(inertia);

        List<History.HistoryEntry<T>> relevantEntries = historyFromSystem(systemHistory)
                .stream()
                .filter(d -> d.date.isAfter(oldestPossible))
                .collect(Collectors.toList());


        Double average = average(relevantEntries);
        if (isAboveThreshold(average)) {
            if (!isAboveThreshold) {
                isAboveThreshold = true;
                return Optional.of(new MonitorEvent<>(
                        now,
                        MonitorEvent.Severity.CRITICAL,
                        MonitorEvent.Type.START,
                        average
                ));
            } else {
                /*return Optional.of(new MonitorEvent<>(
                        now,
                        MonitorEvent.Severity.CRITICAL,
                        MonitorEvent.Type.ONGOING,
                        average
                ));*/
                return Optional.empty();
            }
        } else if (isNearThreshold(average)) {
            return Optional.of(new MonitorEvent<>(
                    now,
                    MonitorEvent.Severity.WARNING,
                    MonitorEvent.Type.STANDALONE,
                    average
            ));
        } else if (isAboveThreshold) {
            isAboveThreshold = false;
            return Optional.of(new MonitorEvent<>(now, MonitorEvent.Severity.NONE, MonitorEvent.Type.STOP, average));
        } else {
            return Optional.empty();
        }

    }

    protected abstract boolean isItem(String id, T item);

    public String getId() {
        return id;
    }

    protected abstract List<History.HistoryEntry<T>> historyFromSystem(List<History.HistoryEntry<SystemLoad>> systemHistory);


    protected abstract Double average(List<History.HistoryEntry<T>> items);

    protected abstract boolean isAboveThreshold(Double value);

    protected abstract boolean isNearThreshold(Double value);
}
