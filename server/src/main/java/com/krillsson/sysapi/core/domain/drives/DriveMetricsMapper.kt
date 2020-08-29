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
package com.krillsson.sysapi.core.domain.drives

import com.krillsson.sysapi.core.domain.sensors.DataType
import com.krillsson.sysapi.core.domain.sensors.HealthData
import com.krillsson.sysapi.core.domain.system.DateMapper
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.HWPartition
import java.time.LocalDateTime

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = [DateMapper::class])
interface DriveMetricsMapper {
    fun map(value: List<Drive>): List<com.krillsson.sysapi.dto.drives.Drive>
    fun mapLoads(value: List<DriveLoad>): List<com.krillsson.sysapi.dto.drives.DriveLoad>
    fun map(value: DriveLoad): com.krillsson.sysapi.dto.drives.DriveLoad
    fun map(value: Drive): com.krillsson.sysapi.dto.drives.Drive
    fun map(value: HealthData): com.krillsson.sysapi.dto.sensors.HealthData
    fun map(value: OsPartition): com.krillsson.sysapi.dto.drives.OsPartition
    fun map(value: DriveSpeed): com.krillsson.sysapi.dto.drives.DriveSpeed
    fun map(value: DriveHealth): com.krillsson.sysapi.dto.drives.DriveHealth
    fun map(value: HWPartition): com.krillsson.sysapi.dto.drives.Partition
    fun map(value: DataType): com.krillsson.sysapi.dto.sensors.DataType
    fun map(value: DriveValues): com.krillsson.sysapi.dto.drives.DriveValues
    fun map(value: Partition): com.krillsson.sysapi.dto.drives.Partition
    fun mapLoadHistory(history: Map<LocalDateTime, List<DriveLoad>>): Map<String, List<com.krillsson.sysapi.dto.drives.DriveLoad>>
    fun mapHistory(history: List<HistoryEntry<List<DriveLoad>>>): List<com.krillsson.sysapi.dto.history.HistoryEntry<List<com.krillsson.sysapi.dto.drives.DriveLoad>>>

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(DriveMetricsMapper::class.java)
    }
}