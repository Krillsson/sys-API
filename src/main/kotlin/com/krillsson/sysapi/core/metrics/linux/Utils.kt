package com.krillsson.sysapi.core.metrics.linux

import org.slf4j.LoggerFactory
import java.io.File

fun readTemperatureFromFile(path: String): Double {
    return try {
        val tempString = File(path).readText().trim()
        val temp = tempString.toInt() / 1000.0
        temp
    } catch (e: Exception) {
        LoggerFactory.getLogger("readTemperatureFromFile").error("Error reading from $path: ${e.message}")
        0.0
    }
}