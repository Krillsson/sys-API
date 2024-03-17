package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.domain.cpu.CpuHealth
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuLoadMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

class WindowsCpuMetrics(
    hal: HardwareAbstractionLayer?,
    operatingSystem: OperatingSystem?,
    defaultCpuLoadMetrics: DefaultCpuLoadMetrics?,
    monitorManager: DelegatingOHMManager
) : DefaultCpuMetrics(hal!!, operatingSystem!!, WindowsCpuSensors(hal, monitorManager), defaultCpuLoadMetrics!!) {
    private class WindowsCpuSensors internal constructor(
        hal: HardwareAbstractionLayer?,
        private val monitorManager: DelegatingOHMManager
    ) : DefaultCpuSensors(hal!!) {
        override fun cpuHealth(): CpuHealth {
            monitorManager.update()
            return super.cpuHealth()
        }

        override fun cpuTemperatures(): List<Double> {
            return monitorManager.CpuMonitors().firstOrNull()?.temperatures?.map { it.value }
                ?: listOf(monitorManager.CpuMonitors().firstOrNull()?.packageTemperature?.value ?: -1.0)
        }

        override fun cpuFanRpm(): Double {
            return monitorManager.CpuMonitors().firstOrNull()?.fanRPM?.value ?: -1.0
        }

        override fun cpuFanPercent(): Double {
            return monitorManager.CpuMonitors().firstOrNull()?.fanPercent?.value ?: -1.0

        }

        override fun cpuVoltage(): Double {
            return monitorManager.CpuMonitors().firstOrNull()?.voltage?.value ?: -1.0
        }
    }
}
