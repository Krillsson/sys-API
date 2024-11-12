package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.domain.cpu.CpuHealth
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer

@Component
class WindowsCpuSensors(
    hal: HardwareAbstractionLayer,
    private val monitorManager: OHMManager
) : DefaultCpuSensors(hal) {
    override fun cpuHealth(): CpuHealth {
        monitorManager.update()
        return super.cpuHealth()
    }

    override fun cpuTemperatures(): List<Double> {
        return monitorManager.cpuMonitors().firstOrNull()?.temperatures?.map { it.value }
            ?: listOf(monitorManager.cpuMonitors().firstOrNull()?.packageTemperature?.value ?: -1.0)
    }

    override fun cpuFanRpm(): Double {
        return monitorManager.cpuMonitors().firstOrNull()?.fanRPM?.value ?: -1.0
    }

    override fun cpuFanPercent(): Double {
        return monitorManager.cpuMonitors().firstOrNull()?.fanPercent?.value ?: -1.0

    }

    override fun cpuVoltage(): Double {
        return monitorManager.cpuMonitors().firstOrNull()?.voltage?.value ?: -1.0
    }
}