package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import oshi.hardware.GlobalMemory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class MetricsHistoryManager extends HistoryManager {
    public MetricsHistoryManager(ScheduledExecutorService executorService) {
        super(executorService);
    }

    public MetricsHistoryManager initializeWith(MetricsFactory provider) {
        insert(CpuLoad.class, new History() {
            @Override
            Supplier getCurrent() {
                return () -> provider.cpuMetrics().cpuLoad();
            }
        });

        insert(DriveLoad.class, new History() {
            @Override
            Supplier getCurrent() {
                return () -> provider.driveMetrics().driveLoads();
            }
        });

        insert(GpuLoad.class, new History() {
            @Override
            Supplier getCurrent() {
                return () -> provider.gpuMetrics().gpuLoads();
            }
        });

        insert(NetworkInterfaceLoad.class, new History() {
            @Override
            Supplier getCurrent() {
                return () -> provider.networkMetrics().networkInterfaceLoads();
            }
        });

        insert(GlobalMemory.class, new History() {

            @Override
            Supplier getCurrent() {
                return () -> provider.memoryMetrics().globalMemory();
            }
        });

        //TODO: this is generating a lot of redundant data in RAM. Maybe merge all maps upon query?
        insert(SystemLoad.class, new History<SystemLoad>() {
            @Override
            Supplier<SystemLoad> getCurrent() {
                return (com.google.common.base.Supplier<SystemLoad>) () -> new SystemLoad(
                        provider.cpuMetrics().cpuLoad(),
                        provider.networkMetrics().networkInterfaceLoads(),
                        provider.driveMetrics().driveLoads(),
                        provider.memoryMetrics().globalMemory(),
                        provider.gpuMetrics().gpuLoads(),
                        provider.motherboardMetrics().motherboardHealth()
                );

            }
        });
        return this;
    }

    public Map<LocalDateTime, CpuLoad> cpuLoadHistory() {
        return get(CpuLoad.class);
    }

    public Map<LocalDateTime, List<com.krillsson.sysapi.dto.drives.DriveLoad>> driveLoadHistory() {
        return get(DriveLoad.class);
    }

    public Map<LocalDateTime, List<GpuLoad>> gpuLoadHistory() {
        return get(GpuLoad.class);
    }

    public Map<LocalDateTime, GlobalMemory> memoryHistory() {
        return get(GlobalMemory.class);
    }

    public Map<LocalDateTime, List<NetworkInterfaceLoad>> networkInterfaceLoadHistory() {
        return get(NetworkInterfaceLoad.class);
    }
}
