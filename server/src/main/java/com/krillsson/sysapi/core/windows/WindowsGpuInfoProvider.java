package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultGpuInfoProvider;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.util.Streams;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsGpuInfoProvider extends DefaultGpuInfoProvider {

    private final MonitorManager monitorManager;


    public WindowsGpuInfoProvider(HardwareAbstractionLayer hal, MonitorManager monitorManager) {
        super(hal);
        this.monitorManager = monitorManager;
    }

    @Override
    public List<Gpu> gpus() {
        monitorManager.Update();
        return Streams.ofNullable(monitorManager.GpuMonitors()).map(m -> new Gpu(
                m.getVendor(),
                m.getName(),
                nullSafeGetValue(m.getCoreClock()),
                nullSafeGetValue(m.getMemoryClock())
        )).collect(Collectors.toList());
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        monitorManager.Update();
        return Streams.ofNullable(monitorManager.GpuMonitors())
                .map(g -> new GpuLoad(
                        nullSafeGetValue(g.getCoreLoad()),
                        nullSafeGetValue(g.getMemoryLoad()),
                        new GpuHealth(
                                nullSafeGetValue(g.getFanRPM()),
                                nullSafeGetValue(g.getFanPercent()),
                                nullSafeGetValue(g.getTemperature())
                        )
                )).collect(Collectors.toList());
    }
}
