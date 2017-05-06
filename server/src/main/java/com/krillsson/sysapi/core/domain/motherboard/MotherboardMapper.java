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

package com.krillsson.sysapi.core.domain.motherboard;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MotherboardMapper {

    MotherboardMapper INSTANCE = Mappers.getMapper(MotherboardMapper.class);

    com.krillsson.sysapi.dto.motherboard.Motherboard map(com.krillsson.sysapi.core.domain.motherboard.Motherboard value);

    com.krillsson.sysapi.dto.motherboard.ComputerSystem map(oshi.hardware.ComputerSystem value);

    com.krillsson.sysapi.dto.motherboard.UsbDevice map(oshi.hardware.UsbDevice value);

    com.krillsson.sysapi.dto.motherboard.Firmware map(oshi.hardware.Firmware value);

    com.krillsson.sysapi.dto.motherboard.Baseboard map(oshi.hardware.Baseboard value);
}
