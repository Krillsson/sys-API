package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskManager
import oshi.hardware.HWDiskStore
import oshi.hardware.HardwareAbstractionLayer
import java.time.Clock

class DiskReadWriteRateMeasurementManager(
    clock: Clock,
    private val hal: HardwareAbstractionLayer,
    taskManager: TaskManager
) : SpeedMeasurementManager(clock, Task.Key.RecordDiskReadWriteRate) {
    init {
        taskManager.registerTask(this)
    }

    private var diskStores = emptyList<HWDiskStore>()
    fun registerStores(){
        for (store in hal.diskStores) {
            register(DiskSpeedSource(store.name))
        }
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