package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Component
import java.io.File

@Component
class LinuxSensorDiscovery {

    companion object {
        private const val THERMAL_SYS = "thermal"
        private const val HWMON_SYS = "hwmon"
    }

    private val logger by logger()

    private val hwmonBase = File("/sys/class/$HWMON_SYS/")
    private val thermalBase = File("/sys/class/$THERMAL_SYS/")

    sealed interface LinuxSensor {
        val name: String
        val identifier: String

        fun readTemperature(): Double

        data class ThermalZone(override val name: String, private val path: String) : LinuxSensor {
            override val identifier: String
                get() = "${THERMAL_SYS}:$name"

            override fun readTemperature(): Double {
                return readTemperatureFromFile(path)
            }
        }

        data class Hwmon(override val name: String, private val paths: List<String>) : LinuxSensor {
            override val identifier: String
                get() = "${HWMON_SYS}:$name"

            override fun readTemperature(): Double {
                val temps = paths.map { readTemperatureFromFile(it) }
                return if (temps.isNotEmpty()) temps.average() else 0.0
            }
        }
    }

    fun initializeSensors(): List<LinuxSensor> {
        val sensorsList = buildList {
            addAll(findThermalSensors())
            addAll(findHwmonSensors())
        }

        if (sensorsList.isEmpty()) {
            logger.warn("No sensors found!")
        } else {
            logger.info("Available Sensors:")
            sensorsList.forEach { sensor ->
                logger.info("${sensor.identifier} ${sensor.readTemperature()} C")
            }
        }
        return sensorsList
    }

    private fun findThermalSensors(): List<LinuxSensor> {
        val thermalSensors = mutableListOf<LinuxSensor>()

        thermalBase.listFiles()?.forEach { zone ->
            if (zone.name.startsWith("${THERMAL_SYS}_zone")) {
                val typeFile = File(zone, "type")
                val tempFile = File(zone, "temp")

                if (typeFile.exists() && tempFile.exists()) {
                    val type = typeFile.readText().trim()
                    thermalSensors.add(LinuxSensor.ThermalZone(type, tempFile.absolutePath))
                }
            }
        }
        return thermalSensors
    }

    private fun findHwmonSensors(): List<LinuxSensor> {
        val hwmonSensors = mutableListOf<LinuxSensor>()

        hwmonBase.listFiles()?.forEach { hwmon ->
            val nameFile = File(hwmon, "name")

            if (nameFile.exists()) {
                val sensorName = nameFile.readText().trim()
                val tempFiles = hwmon.listFiles()?.filter { it.name.matches(Regex("temp\\d+_input")) } ?: emptyList()

                if (tempFiles.isNotEmpty()) {
                    hwmonSensors.add(LinuxSensor.Hwmon(sensorName, tempFiles.map { it.absolutePath }))
                }
            }
        }
        return hwmonSensors
    }
}