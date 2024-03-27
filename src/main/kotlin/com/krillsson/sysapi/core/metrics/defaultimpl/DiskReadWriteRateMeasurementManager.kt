package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import oshi.hardware.HWDiskStore
import oshi.hardware.HardwareAbstractionLayer
import java.time.Clock
import java.util.concurrent.TimeUnit

@Component
class DiskReadWriteRateMeasurementManager(
    clock: Clock,
    private val hal: HardwareAbstractionLayer
) : SpeedMeasurementManager(clock) {

    private var diskStores = emptyList<HWDiskStore>()
    fun registerStores() {
        for (store in hal.diskStores) {
            register(DiskSpeedSource(store.name))
        }
    }

    /*
*
* often: 5s
lessOften: 15s
seldom:5min
verySeldom:30min
*
* */
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    fun runMeasurement() {
        run()
    }

    override fun onUpdateStarted() {
        diskStores = hal.diskStores
    }

    private inner class DiskSpeedSource(
        override val name: String,
    ) : SpeedSource {
        override fun currentRead(): Long {
            return diskStores.firstOrNull { it.name == name }?.readBytes ?: 0
        }

        override fun currentWrite(): Long {
            return diskStores.firstOrNull { it.name == name }?.writeBytes ?: 0
        }
    }
}