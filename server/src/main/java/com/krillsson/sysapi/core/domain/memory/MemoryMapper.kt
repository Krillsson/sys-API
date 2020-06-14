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
package com.krillsson.sysapi.core.domain.memory

import com.krillsson.sysapi.core.domain.system.DateMapper
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = [DateMapper::class])
interface MemoryMapper {
    fun map(load: MemoryLoad): com.krillsson.sysapi.dto.memory.MemoryLoad
    fun mapHistory(history: Map<LocalDateTime, MemoryLoad>): Map<String, com.krillsson.sysapi.dto.memory.MemoryLoad>
    fun mapHistory(history: List<HistoryEntry<MemoryLoad>>): List<com.krillsson.sysapi.dto.history.HistoryEntry<com.krillsson.sysapi.dto.memory.MemoryLoad>>

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(MemoryMapper::class.java)
    }
}