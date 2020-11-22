package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CpuHealth
import oshi.hardware.HardwareAbstractionLayer
import java.util.Arrays

open class DefaultCpuSensors(private val hal: HardwareAbstractionLayer) {
    open fun cpuHealth(): CpuHealth {
        return CpuHealth(
            cpuTemperatures(),
            cpuVoltage(),
            cpuFanRpm(),
            cpuFanPercent()
        )
    }

    open fun cpuVoltage(): Double {
        return hal.sensors.cpuVoltage
    }

    open fun cpuTemperatures(): List<Double> {
        return listOf(hal.sensors.cpuTemperature)
    }

    open fun cpuFanRpm(): Double {
        return hal.sensors.fanSpeeds.firstOrNull()?.toDouble() ?: 0.0
    }

    open fun cpuFanPercent(): Double {
        return 0.0
    }
}