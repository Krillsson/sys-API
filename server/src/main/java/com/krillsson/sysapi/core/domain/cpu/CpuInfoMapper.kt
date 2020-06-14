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
package com.krillsson.sysapi.core.domain.cpu

import com.krillsson.sysapi.core.domain.system.DateMapper
import com.krillsson.sysapi.core.history.HistoryEntry
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.CentralProcessor
import java.time.LocalDateTime

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = [DateMapper::class])
interface CpuInfoMapper {
    fun map(value: CpuInfo?): com.krillsson.sysapi.dto.cpu.CpuInfo?
    fun map(value: CentralProcessor): com.krillsson.sysapi.dto.cpu.CentralProcessor? {
        return com.krillsson.sysapi.dto.cpu.CentralProcessor(
            value.logicalProcessorCount,
            value.physicalProcessorCount,
            value.processorIdentifier.name,
            value.processorIdentifier.identifier,
            value.processorIdentifier.family,
            value.processorIdentifier.vendor,
            value.processorIdentifier.vendorFreq,
            value.processorIdentifier.model,
            value.processorIdentifier.stepping,
            value.processorIdentifier.isCpu64bit
        )
    }

    fun map(value: CpuHealth?): com.krillsson.sysapi.dto.cpu.CpuHealth?
    fun map(value: CpuLoad?): com.krillsson.sysapi.dto.cpu.CpuLoad?
    fun map(value: CoreLoad?): com.krillsson.sysapi.dto.cpu.CoreLoad?
    fun mapLoadHistory(history: Map<LocalDateTime?, CpuLoad?>?): Map<String?, com.krillsson.sysapi.dto.cpu.CpuLoad?>?
    fun mapHistory(history: List<HistoryEntry<CpuLoad?>?>?): List<com.krillsson.sysapi.dto.history.HistoryEntry<com.krillsson.sysapi.dto.cpu.CpuLoad?>?>?

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(CpuInfoMapper::class.java)
    }
}