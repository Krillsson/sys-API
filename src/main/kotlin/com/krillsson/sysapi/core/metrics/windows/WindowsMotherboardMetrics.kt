package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.domain.sensors.DataType
import com.krillsson.sysapi.core.domain.sensors.HealthData
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMotherboardMetrics
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer

@Component
class WindowsMotherboardMetrics(hal: HardwareAbstractionLayer, private val monitorManager: OHMManager) :
    DefaultMotherboardMetrics(hal) {
    override fun motherboardHealth(): List<HealthData> {
        return monitorManager.mainboardMonitor?.let { mm ->
            buildList {
                addAll(mm.getBoardTemperatures()
                    .map { s -> HealthData(s.getLabel(), s.getValue(), DataType.CELCIUS) })
                addAll(mm.getBoardFanPercent()
                    .map { s -> HealthData(s.getLabel(), s.getValue(), DataType.PERCENT) })
                addAll(mm.getBoardFanRPM()
                    .map { s -> HealthData(s.getLabel(), s.getValue(), DataType.RPM) })
            }
        }.orEmpty()

    }
}
