/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveHealth
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.drives.DriveSpeed
import com.krillsson.sysapi.core.domain.drives.DriveValues
import com.krillsson.sysapi.core.domain.drives.OsPartition
import com.krillsson.sysapi.core.domain.drives.Partition
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.Empty
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.CurrentSpeed
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.SpeedSource
import org.apache.commons.lang3.StringUtils
import oshi.hardware.HWDiskStore
import oshi.hardware.HWPartition
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.FileSystem
import oshi.software.os.OperatingSystem
import java.util.Optional
import java.util.stream.Collectors

open class DefaultDriveMetrics(
    private val operatingSystem: OperatingSystem,
    private val hal: HardwareAbstractionLayer,
    private val speedMeasurementManager: SpeedMeasurementManager
) : DriveMetrics {
    fun register() {
        for (store in hal.diskStores) {
            speedMeasurementManager.register(DiskSpeedSource(store.name, hal))
        }
    }

    override fun drives(): List<Drive> {
        return hal.diskStores.map { store ->
            store.asDrive()
        }
    }

    override fun driveLoads(): List<DriveLoad> {
        return hal.diskStores.stream().map { d: HWDiskStore -> createDiskLoad(d) }
            .collect(Collectors.toList())
    }

    override fun driveLoadByName(name: String): Optional<DriveLoad> {
        return hal.diskStores.stream()
            .filter { n: HWDiskStore -> n.name.equals(name, ignoreCase = true) }
            .map { d: HWDiskStore -> createDiskLoad(d) }
            .findAny()
    }

    private fun diskMetrics(
        disk: HWDiskStore,
        partition: OsPartition,
        fileSystem: FileSystem
    ): DriveValues {
        return DriveValues(
            partition.usableSpace,
            partition.totalSpace,
            fileSystem.openFileDescriptors,
            fileSystem.maxFileDescriptors,
            disk.reads,
            disk.readBytes,
            disk.writes,
            disk.writeBytes
        )
    }

    override fun driveByName(name: String): Optional<Drive> {
        return hal.diskStores.stream()
            .filter { n: HWDiskStore -> n.name.equals(name, ignoreCase = true) }
            .map { store: HWDiskStore ->
                store.asDrive()
            }.findAny()
    }

    protected open fun diskSpeedForStore(
        diskStore: HWDiskStore,
        osFileStore: OsPartition?
    ): Optional<DriveSpeed?> {
        val currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(
            diskStore.name
        )
        return currentSpeedForName.map { s: CurrentSpeed ->
            DriveSpeed(
                s.readPerSeconds,
                s.writePerSeconds
            )
        }
    }

    open fun diskHealth(name: String?): DriveHealth {
        return Empty.DRIVE_HEALTH
    }

    private fun findAssociatedFileStore(diskStore: HWDiskStore): Optional<OsPartition> {
        val fileStores = operatingSystem.fileSystem.fileStores
        val partitions = diskStore.partitions

        val fileStoreIds = fileStores.map { it.uuid }
        val partitionIds = partitions.map { it.uuid }
        fileStoreIds.intersect(partitionIds)

        val intersectedId = fileStores
            .map { it.uuid }
            .intersect(partitions.map { it.uuid })
            .firstOrNull()

        return intersectedId?.let { id ->
            val osStore = fileStores.first { it.uuid == id }
            val partition = partitions.first { it.uuid == id }
            Optional.of(
                OsPartition(
                    partition.identification,
                    partition.name,
                    osStore.type,
                    partition.uuid,
                    partition.size,
                    partition.major,
                    partition.minor,
                    osStore.mount,
                    osStore.volume,
                    osStore.logicalVolume,
                    osStore.mount,
                    osStore.description,
                    osStore.usableSpace,
                    osStore.totalSpace
                )
            )
        } ?: Optional.empty()
    }

    private fun createDiskLoad(d: HWDiskStore): DriveLoad {
        val partition = findAssociatedFileStore(d).orElse(Empty.OS_PARTITION)
        val health = diskHealth(d.name)
        val metrics = diskMetrics(d, partition, operatingSystem.fileSystem)
        val speed = diskSpeedForStore(d, partition).orElse(Empty.DRIVE_SPEED)
        return DriveLoad(d.name, getSerial(d), metrics, speed!!, health)
    }

    private fun getSerial(d: HWDiskStore): String {
        return if (StringUtils.isEmpty(d.serial)) "n/a" else d.serial
    }

    private class DiskSpeedSource constructor(
        private val name: String,
        private val hal: HardwareAbstractionLayer
    ) : SpeedSource {
        override fun getName(): String {
            return name
        }

        override fun getCurrentRead(): Long {
            return hal.diskStores.stream()
                .filter { d: HWDiskStore -> d.name == name }
                .map { hwDiskStore: HWDiskStore ->
                    hwDiskStore.updateAttributes()
                    hwDiskStore.readBytes
                }
                .findAny()
                .orElse(0L)
        }

        override fun getCurrentWrite(): Long {
            return hal.diskStores.stream()
                .filter { d: HWDiskStore -> d.name == name }
                .map { hwDiskStore: HWDiskStore ->
                    hwDiskStore.updateAttributes()
                    hwDiskStore.writeBytes
                }
                .findAny()
                .orElse(0L)
        }
    }

    private fun HWDiskStore.asDrive() = Drive(
        model,
        name,
        serial.takeIf { !it.isNullOrBlank() } ?: "N/A",
        size,
        findAssociatedFileStore(this).orElse(Empty.OS_PARTITION),
        partitions.asPartitions()
    )

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

    private fun List<HWPartition>.asPartitions() = map { it.asPartition() }
}