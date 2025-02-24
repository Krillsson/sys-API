package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors
import com.krillsson.sysapi.util.logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import java.io.File

@Component
@Lazy
class LinuxCpuSensors(hal: HardwareAbstractionLayer, config: YAMLConfigFile) : DefaultCpuSensors(hal) {

    private val logger by logger()

    private val hwmonBase = File("/sys/class/hwmon/")
    private val thermalBase = File("/sys/class/thermal/")
    private val configuredSensor = config.linux.overrideCpuTempSensor
    private val possibleCpuSensors = listOf("x86_pkg_temp", "Tdie", "Tctl", "coretemp", "k10temp")
    private val sensors = initializeSensors()


    private sealed interface Sensor {
        val name: String

        fun readTemperature(): Double

        data class ThermalZone(override val name: String, private val path: String) : Sensor {
            override fun readTemperature(): Double {
                return readTemperatureFromFile(path)
            }
        }

        data class Hwmon(override val name: String, private val paths: List<String>) : Sensor {
            override fun readTemperature(): Double {
                val temps = paths.map { readTemperatureFromFile(it) }
                return if (temps.isNotEmpty()) temps.average() else 0.0
            }
        }
    }

    private fun initializeSensors(): List<Sensor> {
        val sensorsList = buildList {
            addAll(findThermalSensors())
            addAll(findHwmonSensors())
        }

        if (sensorsList.isEmpty()) {
            logger.warn("No sensors found!")
        } else {
            logger.info("Available Sensors:")
            sensorsList.forEachIndexed { index, sensor ->
                logger.info("${sensor::class.simpleName}:${sensor.name} ${sensor.readTemperature()} C")
            }
        }
        return sensorsList
    }

    private fun findThermalSensors(): List<Sensor> {
        val thermalSensors = mutableListOf<Sensor>()

        thermalBase.listFiles()?.forEach { zone ->
            if (zone.name.startsWith("thermal_zone")) {
                val typeFile = File(zone, "type")
                val tempFile = File(zone, "temp")

                if (typeFile.exists() && tempFile.exists()) {
                    val type = typeFile.readText().trim()
                    thermalSensors.add(Sensor.ThermalZone(type, tempFile.absolutePath))
                }
            }
        }
        return thermalSensors
    }

    private fun findHwmonSensors(): List<Sensor> {
        val hwmonSensors = mutableListOf<Sensor>()

        hwmonBase.listFiles()?.forEach { hwmon ->
            val nameFile = File(hwmon, "name")

            if (nameFile.exists()) {
                val sensorName = nameFile.readText().trim()
                val tempFiles = hwmon.listFiles()?.filter { it.name.matches(Regex("temp\\d+_input")) } ?: emptyList()

                if (tempFiles.isNotEmpty()) {
                    hwmonSensors.add(Sensor.Hwmon(sensorName, tempFiles.map { it.absolutePath }))
                }
            }
        }
        return hwmonSensors
    }

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
            logger.info("No match found: returning value from OSHI: $oshiValue")
            result = oshiValue.firstOrNull()
        }

        return listOf(result ?: 0.0)
    }

    private fun readValueFromBaseSensorsSet(): Double? {
        var result: Double? = null
        for (possibleSensor in possibleCpuSensors) {
            val readTemperature = sensors.firstOrNull { it.name == possibleSensor }?.readTemperature()
            if (readTemperature != null) {
                logger.info("Base set match found. Returning $possibleSensor $readTemperature")
                result = readTemperature
            }
            break
        }
        return result
    }

    private fun readValueFromConfiguredSensor(): Double? {
        return sensors.firstOrNull { it.name == configuredSensor }?.readTemperature()?.let { value ->
            logger.info("User configured value found. Returning $configuredSensor $value")
            value
        }
    }

}

private fun readTemperatureFromFile(path: String): Double {
    return try {
        val tempString = File(path).readText().trim()
        val temp = tempString.toInt() / 1000.0
        temp
    } catch (e: Exception) {
        LoggerFactory.getLogger("readTemperatureFromFile").error("Error reading from $path: ${e.message}")
        0.0
    }
}

