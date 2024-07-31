package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuHealth
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultGpuMetrics
import com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import java.util.stream.Collectors

@Component
class WindowsGpuMetrics(hal: HardwareAbstractionLayer, private val monitorManager: OHMManager) :
    DefaultGpuMetrics(hal) {
    override fun gpus(): List<Gpu> {
        monitorManager.update()
        return monitorManager.gpuMonitors().map { monitor ->
            Gpu(
                monitor.getVendor(),
                monitor.getName(),
                NullSafeOhmMonitor.nullSafeGetValue(monitor.getCoreClock()),
                NullSafeOhmMonitor.nullSafeGetValue(monitor.getMemoryClock())
            )
        }
    }

    override fun gpuLoads(): List<GpuLoad> {
        monitorManager.update()
        return monitorManager.gpuMonitors().map { monitor ->
            GpuLoad(
                monitor.getName(), NullSafeOhmMonitor.nullSafeGetValue(monitor.getCoreLoad()),
                NullSafeOhmMonitor.nullSafeGetValue(monitor.getMemoryLoad()),
                GpuHealth(
                    NullSafeOhmMonitor.nullSafeGetValue(monitor.getFanRPM()),
                    NullSafeOhmMonitor.nullSafeGetValue(monitor.getFanPercent()),
                    NullSafeOhmMonitor.nullSafeGetValue(monitor.getTemperature())
                )
            )
        }

    }
}
