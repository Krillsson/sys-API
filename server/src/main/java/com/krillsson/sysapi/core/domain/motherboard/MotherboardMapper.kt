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
package com.krillsson.sysapi.core.domain.motherboard

import com.krillsson.sysapi.core.domain.system.DateMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.Baseboard
import oshi.hardware.ComputerSystem
import oshi.hardware.Firmware
import oshi.hardware.UsbDevice
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = [DateMapper::class])
interface MotherboardMapper {
    fun map(value: Motherboard?): com.krillsson.sysapi.dto.motherboard.Motherboard?
    fun map(value: ComputerSystem?): com.krillsson.sysapi.dto.motherboard.ComputerSystem?
    fun map(value: UsbDevice?): com.krillsson.sysapi.dto.motherboard.UsbDevice?
    fun map(value: Firmware?): com.krillsson.sysapi.dto.motherboard.Firmware?
    fun map(value: Baseboard?): com.krillsson.sysapi.dto.motherboard.Baseboard?
    fun map(value: String?): Date? {
        return try {
            if (value != null && !value.equals(
                    "unknown",
                    ignoreCase = true
                )
            ) SimpleDateFormat().parse(value) else null
        } catch (e: ParseException) {
            null
        }
    }

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(MotherboardMapper::class.java)
    }
}