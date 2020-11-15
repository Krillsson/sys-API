package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultGpuMetrics;
import com.krillsson.sysapi.util.Streams;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsGpuMetrics extends DefaultGpuMetrics {

    private final DelegatingMonitorManager monitorManager;


    public WindowsGpuMetrics(HardwareAbstractionLayer hal, DelegatingMonitorManager monitorManager) {
        super(hal);
        this.monitorManager = monitorManager;
    }

    @Override
    public List<Gpu> gpus() {
        monitorManager.update();
        return Streams.ofNullable(monitorManager.GpuMonitors()).map(m -> new Gpu(
                m.getVendor(),
                m.getName(),
                nullSafeGetValue(m.getCoreClock()),
                nullSafeGetValue(m.getMemoryClock())
        )).collect(Collectors.toList());
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        monitorManager.update();
        return Streams.ofNullable(monitorManager.GpuMonitors())
                .map(g -> new GpuLoad(
                        g.getName(), nullSafeGetValue(g.getCoreLoad()),
                        nullSafeGetValue(g.getMemoryLoad()),
                        new GpuHealth(
                                nullSafeGetValue(g.getFanRPM()),
                                nullSafeGetValue(g.getFanPercent()),
                                nullSafeGetValue(g.getTemperature())
                        )
                )).collect(Collectors.toList());
    }
}
