package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.disk.*
import com.krillsson.sysapi.core.metrics.DiskMetrics
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import org.apache.commons.lang3.StringUtils
import oshi.hardware.HWDiskStore
import oshi.hardware.HWPartition
import oshi.hardware.HardwareAbstractionLayer
import java.util.*

open class DefaultDiskMetrics(
    private val hal: HardwareAbstractionLayer,
    private val speedMeasurementManager: SpeedMeasurementManager
): DiskMetrics {

    fun register() {
        for (store in hal.diskStores) {
            speedMeasurementManager.register(DiskSpeedSource(store.name, store))
        }
    }

    override fun disks(): List<Disk> {
        return hal.diskStores.map { store ->
            store.asDisk()
        }
    }

    override fun diskLoads(): List<DiskLoad> {
        return hal.diskStores.map { d: HWDiskStore -> createDiskLoad(d) }
    }

    override fun diskLoadByName(name: String): DiskLoad? {
        return hal.diskStores
            .filter { n: HWDiskStore -> n.name.equals(name, ignoreCase = true) }
            .map { d: HWDiskStore -> createDiskLoad(d) }
            .firstOrNull()
    }

    private fun diskMetrics(
        disk: HWDiskStore,
    ): DiskValues {
        return DiskValues(
            disk.reads,
            disk.readBytes,
            disk.writes,
            disk.writeBytes
        )
    }

    override fun diskByName(name: String): Disk? {
        return hal.diskStores
            .filter { n: HWDiskStore -> n.name.equals(name, ignoreCase = true) }
            .map { store: HWDiskStore ->
                store.asDisk()
            }.firstOrNull()
    }

    protected open fun diskSpeedForStore(
        diskStore: HWDiskStore,
    ): Optional<DiskSpeed?> {
        val currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(
            diskStore.name
        )
        return currentSpeedForName.map { s: SpeedMeasurementManager.CurrentSpeed ->
            DiskSpeed(
                s.readPerSeconds,
                s.writePerSeconds
            )
        }
    }

    open fun diskHealth(name: String?): DiskHealth {
        return Empty.DISK_HEALTH
    }

    private fun createDiskLoad(diskStore: HWDiskStore): DiskLoad {
        val health = diskHealth(diskStore.name)
        val metrics = diskMetrics(diskStore)
        val speed = diskSpeedForStore(diskStore).orElse(Empty.DISK_SPEED)
        return DiskLoad(diskStore.name, getSerial(diskStore), metrics, speed!!, health)
    }

    private fun getSerial(d: HWDiskStore): String {
        return if (StringUtils.isEmpty(d.serial)) "n/a" else d.serial
    }

    private class DiskSpeedSource constructor(
        private val name: String,
        private val hwDiskStore: HWDiskStore
    ) : SpeedMeasurementManager.SpeedSource {
        override fun getName(): String {
            return name
        }

        override fun getCurrentRead(): Long {
            hwDiskStore.updateAttributes()
            return hwDiskStore.readBytes
        }

        override fun getCurrentWrite(): Long {
            hwDiskStore.updateAttributes()
            return hwDiskStore.writeBytes
        }
    }

    private fun HWDiskStore.asDisk() = Disk(
        model,
        name,
        serial.takeIf { !it.isNullOrBlank() } ?: "N/A",
        size,
        partitions.asPartitions()
    )

    private fun List<HWPartition>.asPartitions() = map { it.asPartition() }

    private fun HWPartition.asPartition() = Partition(
        identification,
        name,
        type,
        uuid,
        size,
        major,
        minor,
        mountPoint
    )
}