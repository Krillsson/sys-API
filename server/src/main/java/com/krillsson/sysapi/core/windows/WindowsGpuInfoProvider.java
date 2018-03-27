package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultGpuInfoProvider;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor;
import ohmwrapper.GpuMonitor;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafe;

public class WindowsGpuInfoProvider extends DefaultGpuInfoProvider {

    private final MonitorManager monitorManager;


    public WindowsGpuInfoProvider(HardwareAbstractionLayer hal, MonitorManager monitorManager) {
        super(hal);
        this.monitorManager = monitorManager;
    }


    @Override
    public List<Gpu> gpus() {
        List<Gpu> gpus = new ArrayList<>();
        monitorManager.Update();
        final GpuMonitor[] gpuMonitors = monitorManager.GpuMonitors();
        if (gpuMonitors != null && gpuMonitors.length > 0) {
            gpus.addAll(Stream.of(gpuMonitors)
                                .map(m -> new Gpu(
                                        m.getVendor(),
                                        m.getName(),
                                        Optional.ofNullable(m.getCoreClock())
                                                .orElse(new NullSafeOhmMonitor.NullSafeOHMSensor())
                                                .getValue(),
                                        Optional.ofNullable(m.getMemoryClock())
                                                .orElse(new NullSafeOhmMonitor.NullSafeOHMSensor())
                                                .getValue()
                                )).collect(Collectors.toList()));
        }
        return gpus;
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        return super.gpuLoads();
    }

    public Map<String, GpuHealth> gpuHealths() {
        Map<String, GpuHealth> gpus = new HashMap<>();
        monitorManager.Update();
        final GpuMonitor[] gpuMonitors = monitorManager.GpuMonitors();
        if (gpuMonitors != null && gpuMonitors.length > 0) {
            for (GpuMonitor gpuMonitor : gpuMonitors) {
                GpuHealth gpuHealth = new GpuHealth(
                        nullSafe(gpuMonitor.getFanRPM()).getValue(),
                        nullSafe(gpuMonitor.getFanPercent()).getValue(),
                        nullSafe(gpuMonitor.getTemperature()).getValue(),
                        nullSafe(gpuMonitor.getCoreLoad()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue()
                );
                gpus.put(gpuMonitor.getName(), gpuHealth);
            }
        }
        return gpus;
    }
}
