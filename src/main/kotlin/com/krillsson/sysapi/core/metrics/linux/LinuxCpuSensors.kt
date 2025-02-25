package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer

@Component
@Lazy
class LinuxCpuSensors(hal: HardwareAbstractionLayer, config: YAMLConfigFile, linuxSensorDiscovery: LinuxSensorDiscovery) : DefaultCpuSensors(hal) {

    private val configuredSensor = config.linux.overrideCpuTempSensor
    private val possibleCpuSensors = listOf("x86_pkg_temp", "Tdie", "Tctl", "coretemp", "k10temp")
    private val sensors = linuxSensorDiscovery.initializeSensors()

    override fun cpuTemperatures(): List<Double> {
        var result: Double? = null

        if (configuredSensor != null) {
            result = readValueFromConfiguredSensor()
        }

        if (result == null) {
            result = readValueFromBaseSensorsSet()
        }

        if (result == null) {
            val oshiValue = super.cpuTemperatures()
            result = oshiValue.firstOrNull()
        }

        return listOf(result ?: 0.0)
    }

    private fun readValueFromBaseSensorsSet(): Double? {
        var result: Double? = null
        for (possibleSensor in possibleCpuSensors) {
            val readTemperature = sensors.firstOrNull { it.name == possibleSensor }?.readTemperature()
            if (readTemperature != null) {
                result = readTemperature
                break
            }
        }
        return result
    }

    private fun readValueFromConfiguredSensor(): Double? {
        return sensors.firstOrNull { it.identifier == configuredSensor }?.readTemperature()
    }

}

