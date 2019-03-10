package com.krillsson.sysapi.core.history;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MetricsHistoryManager extends HistoryManager {


    public MetricsHistoryManager(HistoryConfiguration configuration, EventBus eventBus) {
        super(configuration, eventBus);
    }

    public List<HistoryEntry<CpuLoad>> cpuLoadHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return systemLoadHistory(fromDate, toDate).stream()
                .map(e -> new HistoryEntry<CpuLoad>(e.date, e.value.getCpuLoad()))
                .collect(Collectors.toList());
    }

    public List<HistoryEntry<SystemLoad>> systemLoadHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return getHistoryLimitedToDates(fromDate, toDate);
    }

    public List<HistoryEntry<List<DriveLoad>>> driveLoadHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return systemLoadHistory(fromDate, toDate).stream()
                .map(e -> new HistoryEntry<List<DriveLoad>>(e.date, e.value.getDriveLoads()))
                .collect(Collectors.toList());
    }

    public List<HistoryEntry<List<GpuLoad>>> gpuLoadHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return systemLoadHistory(fromDate, toDate).stream()
                .map(e -> new HistoryEntry<List<GpuLoad>>(e.date, e.value.getGpuLoads()))
                .collect(Collectors.toList());
    }

    public List<HistoryEntry<MemoryLoad>> memoryHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return systemLoadHistory(fromDate, toDate).stream()
                .map(e -> new HistoryEntry<>(e.date, e.value.getMemory()))
                .collect(Collectors.toList());
    }

    public List<HistoryEntry<List<NetworkInterfaceLoad>>> networkInterfaceLoadHistory(@Nullable OffsetDateTime fromDate, @Nullable OffsetDateTime toDate) {
        return systemLoadHistory(fromDate, toDate).stream()
                .map(e -> new HistoryEntry<>(
                        e.date,
                        e.value.getNetworkInterfaceLoads()
                ))
                .collect(Collectors.toList());
    }
}
