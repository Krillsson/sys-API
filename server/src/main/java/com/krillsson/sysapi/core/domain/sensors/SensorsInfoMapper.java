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

package com.krillsson.sysapi.core.domain.sensors;

import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper;
import com.krillsson.sysapi.core.domain.gpu.GpuInfoMapper;
import com.krillsson.sysapi.core.domain.motherboard.MotherboardMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {MotherboardMapper.class, CpuInfoMapper.class, GpuInfoMapper.class}
)
public interface SensorsInfoMapper {
    SensorsInfoMapper INSTANCE = Mappers.getMapper(SensorsInfoMapper.class);

    com.krillsson.sysapi.dto.sensors.SensorsInfo map(SensorsInfo value);

    com.krillsson.sysapi.dto.sensors.HealthData map(com.krillsson.sysapi.core.domain.sensors.HealthData value);
    List<com.krillsson.sysapi.dto.sensors.HealthData> mapDatas(List<com.krillsson.sysapi.core.domain.sensors.HealthData> value);

    com.krillsson.sysapi.dto.sensors.DataType map(com.krillsson.sysapi.core.domain.sensors.DataType value);
}
