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
package com.krillsson.sysapi.core.domain.processes

import com.krillsson.sysapi.dto.processes.Memory
import com.krillsson.sysapi.dto.processes.ProcessInfo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.GlobalMemory

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ProcessInfoMapper {
    fun map(value: ProcessesInfo?): ProcessInfo?
    fun map(value: GlobalMemory): Memory? {
        val virtualMemory = value.virtualMemory
        return Memory(
            virtualMemory.swapTotal,
            virtualMemory.swapUsed,
            value.total,
            value.available
        )
    }

    fun map(value: Process?): com.krillsson.sysapi.dto.processes.Process?

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(ProcessInfoMapper::class.java)
    }
}