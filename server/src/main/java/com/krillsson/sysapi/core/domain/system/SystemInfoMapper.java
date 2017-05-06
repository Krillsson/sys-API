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

package com.krillsson.sysapi.core.domain.system;

import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper;
import com.krillsson.sysapi.core.domain.memory.GlobalMemoryMapper;
import com.krillsson.sysapi.core.domain.power.PowerSourceMapper;
import com.krillsson.sysapi.core.domain.sensors.SensorsInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {SensorsInfoMapper.class, CpuInfoMapper.class, GlobalMemoryMapper.class, PowerSourceMapper.class}
)
public interface SystemInfoMapper {
    SystemInfoMapper INSTANCE = Mappers.getMapper(SystemInfoMapper.class);

    com.krillsson.sysapi.dto.system.SystemInfo map(com.krillsson.sysapi.core.domain.system.SystemInfo value);

    com.krillsson.sysapi.dto.system.OperatingSystem map(oshi.software.os.OperatingSystem value);

    com.krillsson.sysapi.dto.system.Version map(oshi.software.os.OperatingSystemVersion value);

    com.krillsson.sysapi.dto.system.JvmProperties map(JvmProperties jvmProperties);
}
