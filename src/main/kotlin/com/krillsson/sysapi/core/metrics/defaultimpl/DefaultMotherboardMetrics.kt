package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.sensors.HealthData
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer

@Component
open class DefaultMotherboardMetrics(private val hal: HardwareAbstractionLayer) :
    MotherboardMetrics {
    override fun motherboard(): Motherboard {
        return Motherboard(hal.computerSystem, hal.getUsbDevices(false))
    }

    override fun motherboardHealth(): List<HealthData> {
        return emptyList()
    }
}