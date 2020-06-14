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
package com.krillsson.sysapi.core.domain.gpu

import com.krillsson.sysapi.core.domain.system.DateMapper
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.Display
import oshi.util.EdidUtil
import java.time.LocalDateTime

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = [DateMapper::class])
interface GpuInfoMapper {
    fun map(value: GpuLoad): com.krillsson.sysapi.dto.gpu.GpuLoad
    fun map(value: List<GpuLoad>): List<com.krillsson.sysapi.dto.gpu.GpuLoad>
    fun map(value: Display): com.krillsson.sysapi.dto.gpu.Display
    fun map(value: Gpu): com.krillsson.sysapi.dto.gpu.Gpu
    fun mapGpus(value: List<Gpu>): List<com.krillsson.sysapi.dto.gpu.Gpu>
    fun map(value: GpuHealth): com.krillsson.sysapi.dto.gpu.GpuHealth
    fun map(value: ByteArray): String {
        return EdidUtil.toString(value)
    }

    fun mapLoadHistory(history: Map<LocalDateTime, List<GpuLoad>>): Map<String, List<com.krillsson.sysapi.dto.gpu.GpuLoad>>
    fun mapHistory(history: List<HistoryEntry<List<GpuLoad>>>): List<com.krillsson.sysapi.dto.history.HistoryEntry<List<com.krillsson.sysapi.dto.gpu.GpuLoad>>>

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(GpuInfoMapper::class.java)
    }
}