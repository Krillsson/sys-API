package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.history.History;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DriveMonitor extends Monitor<DriveLoad, Long> {
    private final Long threshold;
    private final Long near;

    public DriveMonitor(String id, Duration inertia, Long threshold, Long near) {
        super(id, inertia);
        this.threshold = threshold;
        this.near = near;
    }

    @Override
    protected boolean isItem(String id, DriveLoad item) {
        return item.getName().equalsIgnoreCase(id);
    }

    @Override
    protected List<History.HistoryEntry<DriveLoad>> historyFromSystem(List<History.HistoryEntry<SystemLoad>> systemHistory) {
        return systemHistory.stream()
                .map(e -> new History.HistoryEntry<DriveLoad>(
                        e.date,
                        e.value.getDriveLoads()
                                .stream()
                                .filter(i -> isItem(getId(), i))
                                .findFirst()
                                .get()
                ))
                .collect(Collectors.toList());
    }

    @Override
    protected Double average(List<History.HistoryEntry<DriveLoad>> items) {
        return items.stream()
                .mapToLong(e -> e.value.getMetrics().getUsableSpace())
                .average()
                .orElse(0d);
    }

    @Override
    protected boolean isAboveThreshold(Double value) {
        return value < threshold;
    }

    @Override
    protected boolean isNearThreshold(Double value) {
        return value < near;
    }

    @Override
    public String toString() {
        return "DriveMonitor{" +
                "threshold=" + threshold +
                ", near=" + near +
                '}';
    }
}
