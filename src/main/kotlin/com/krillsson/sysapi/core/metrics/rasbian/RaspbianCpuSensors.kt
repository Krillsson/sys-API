package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.util.ExecutingCommand
import oshi.util.FileUtil

@Component
class RaspbianCpuSensors(hal: HardwareAbstractionLayer) : DefaultCpuSensors(hal) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(
            RaspbianCpuSensors::class.java
        )
        private const val CPU_TEMP_FILE_LOCATION = "/sys/class/thermal/thermal_zone0/temp"
        private const val VCGENCMD = "vcgencmd"
        private const val VCGENCMD_VOLT = VCGENCMD + "measure_volts core"
    }

    override fun cpuTemperatures(): List<Double> {
        return listOf(FileUtil.getLongFromFile(CPU_TEMP_FILE_LOCATION) / 1000.0)
    }

    public override fun cpuVoltage(): Double {
        val vcgenCmdAnswer = executeCommand()
        if (vcgenCmdAnswer != null && vcgenCmdAnswer.length > 1) {
            val trimmed = vcgenCmdAnswer.replace("volt=", "").replace("V", "")
            return try {
                val value = java.lang.Double.valueOf(trimmed)
                if (value.isInfinite() || value.isNaN()) {
                    0.0
                } else {
                    value
                }
            } catch (e: NumberFormatException) {
                LOGGER.error("Error while parsing vcgencmd command", e)
                0.0
            }
        }
        return super.cpuVoltage()
    }

    open fun executeCommand(): String? {
        return ExecutingCommand.getFirstAnswer(VCGENCMD_VOLT)
    }
}