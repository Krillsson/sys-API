package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.history.History;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CpuMonitor extends Monitor<CpuLoad, Double> {
    private final double threshold;
    private final double near;

    public CpuMonitor(String id, Duration inertia, double threshold, double near) {
        super(id, inertia);
        this.threshold = threshold;
        this.near = near;
    }

    @Override
    protected boolean isItem(String id, CpuLoad item) {
        return true;
    }

    @Override
    protected List<History.HistoryEntry<CpuLoad>> historyFromSystem(List<History.HistoryEntry<SystemLoad>> systemHistory) {
        return systemHistory.stream()
                .map(e -> new History.HistoryEntry<CpuLoad>(e.date, e.value.getCpuLoad()))
                .collect(Collectors.toList());
    }

    @Override
    protected Double average(List<History.HistoryEntry<CpuLoad>> items) {
        return items.stream().mapToDouble(e -> e.value.getCpuLoadOsMxBean()).average().orElse(0d);
    }

    @Override
    protected boolean isAboveThreshold(Double value) {
        return value > threshold;
    }

    @Override
    protected boolean isNearThreshold(Double value) {
        return value > near;
    }

    @Override
    public String toString() {
        return "CpuMonitor{" +
                "threshold=" + threshold +
                ", near=" + near +
                '}';
    }
}
