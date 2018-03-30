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

package com.krillsson.sysapi.core.domain.gpu;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import oshi.util.EdidUtil;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GpuInfoMapper {

    GpuInfoMapper INSTANCE = Mappers.getMapper(GpuInfoMapper.class);


    com.krillsson.sysapi.dto.gpu.GpuLoad map(GpuLoad value);
    List<com.krillsson.sysapi.dto.gpu.GpuLoad> map(List<GpuLoad> value);

    com.krillsson.sysapi.dto.gpu.Display map(oshi.hardware.Display value);

    com.krillsson.sysapi.dto.gpu.Gpu map(com.krillsson.sysapi.core.domain.gpu.Gpu value);
    List<com.krillsson.sysapi.dto.gpu.Gpu> mapGpus(List<com.krillsson.sysapi.core.domain.gpu.Gpu> value);

    com.krillsson.sysapi.dto.gpu.GpuHealth map(com.krillsson.sysapi.core.domain.gpu.GpuHealth value);

    default java.lang.String map(byte[] value) {
        return EdidUtil.toString(value);
    }
}
