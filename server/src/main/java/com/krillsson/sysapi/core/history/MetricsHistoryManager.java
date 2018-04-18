package com.krillsson.sysapi.core.history;

import com.google.common.eventbus.EventBus;
import com.krillsson.sysapi.config.HistoryConfiguration;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import oshi.hardware.GlobalMemory;

import java.util.List;
import java.util.stream.Collectors;

public class MetricsHistoryManager extends HistoryManager {


    public MetricsHistoryManager(HistoryConfiguration configuration, EventBus eventBus) {
        super(configuration, eventBus);
    }

    public List<History.HistoryEntry<CpuLoad>> cpuLoadHistory() {
        return systemLoadHistory().stream()
                .map(e -> new History.HistoryEntry<CpuLoad>(e.date, e.value.getCpuLoad()))
                .collect(Collectors.toList());
    }

    public List<History.HistoryEntry<SystemLoad>> systemLoadHistory() {
        return getHistory();
    }

    public List<History.HistoryEntry<List<DriveLoad>>> driveLoadHistory() {
        return systemLoadHistory().stream()
                .map(e -> new History.HistoryEntry<List<DriveLoad>>(e.date, e.value.getDriveLoads()))
                .collect(Collectors.toList());
    }

    public List<History.HistoryEntry<List<GpuLoad>>> gpuLoadHistory() {
        return systemLoadHistory().stream()
                .map(e -> new History.HistoryEntry<List<GpuLoad>>(e.date, e.value.getGpuLoads()))
                .collect(Collectors.toList());
    }

    public List<History.HistoryEntry<GlobalMemory>> memoryHistory() {
        return systemLoadHistory().stream()
                .map(e -> new History.HistoryEntry<GlobalMemory>(e.date, e.value.getMemory()))
                .collect(Collectors.toList());
    }

    public List<History.HistoryEntry<List<NetworkInterfaceLoad>>> networkInterfaceLoadHistory() {
        return systemLoadHistory().stream()
                .map(e -> new History.HistoryEntry<List<NetworkInterfaceLoad>>(
                        e.date,
                        e.value.getNetworkInterfaceLoads()
                ))
                .collect(Collectors.toList());
    }
}
