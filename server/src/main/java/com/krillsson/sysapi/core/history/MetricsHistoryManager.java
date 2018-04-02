package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import oshi.hardware.GlobalMemory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class MetricsHistoryManager extends HistoryManager {
    public MetricsHistoryManager(ScheduledExecutorService executorService) {
        super(executorService);
    }

    public HistoryManager initializeWith(MetricsFactory provider) {
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
        return this;
    }
}
