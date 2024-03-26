package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.sensors.HealthData

interface MotherboardMetrics {
    fun motherboard(): Motherboard
    fun motherboardHealth(): List<HealthData>
}