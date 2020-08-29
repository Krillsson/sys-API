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
package com.krillsson.sysapi.core.domain.power

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import oshi.hardware.PowerSource

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface PowerSourceMapper {
    fun map(value: PowerSource): com.krillsson.sysapi.dto.power.PowerSource? {
        return com.krillsson.sysapi.dto.power.PowerSource(
            value.name,
            value.currentCapacity.toDouble(),
            value.timeRemainingEstimated
        )
    }

    fun map(value: Array<PowerSource?>?): Array<com.krillsson.sysapi.dto.power.PowerSource?>?

    companion object {
        val INSTANCE = Mappers.getMapper(PowerSourceMapper::class.java)
    }
}