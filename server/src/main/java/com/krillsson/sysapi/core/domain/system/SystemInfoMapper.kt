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
package com.krillsson.sysapi.core.domain.system

import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper
import com.krillsson.sysapi.core.domain.drives.DriveMetricsMapper
import com.krillsson.sysapi.core.domain.gpu.GpuInfoMapper
import com.krillsson.sysapi.core.domain.memory.MemoryMapper
import com.krillsson.sysapi.core.domain.motherboard.MotherboardMapper
import com.krillsson.sysapi.core.domain.network.NetworkInterfacesMapper
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.dto.system.Version
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.PlatformEnum
import oshi.software.os.OperatingSystem
import oshi.software.os.OperatingSystem.OSVersionInfo
import java.time.LocalDateTime

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = [CpuInfoMapper::class, MemoryMapper::class, DateMapper::class, NetworkInterfacesMapper::class, DriveMetricsMapper::class, GpuInfoMapper::class, MotherboardMapper::class]
)
interface SystemInfoMapper {
    fun map(value: PlatformEnum): com.krillsson.sysapi.dto.system.PlatformEnum
    fun map(value: SystemInfo): com.krillsson.sysapi.dto.system.SystemInfo
    fun map(value: SystemLoad): com.krillsson.sysapi.dto.system.SystemLoad
    fun map(value: OperatingSystem): com.krillsson.sysapi.dto.system.OperatingSystem {
        return com.krillsson.sysapi.dto.system.OperatingSystem(
            value.manufacturer,
            value.family,
            Version(
                value.versionInfo.version,
                value.versionInfo.codeName,
                value.versionInfo.buildNumber
            )
        )
    }

    fun map(value: OSVersionInfo): Version
    fun map(jvmProperties: JvmProperties): com.krillsson.sysapi.dto.system.JvmProperties
    fun mapLoadHistory(history: Map<LocalDateTime, SystemLoad>): Map<String, com.krillsson.sysapi.dto.system.SystemLoad>
    fun mapHistory(history: List<HistoryEntry<SystemLoad>>): List<com.krillsson.sysapi.dto.history.HistoryEntry<com.krillsson.sysapi.dto.system.SystemLoad>>

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(SystemInfoMapper::class.java)
    }
}