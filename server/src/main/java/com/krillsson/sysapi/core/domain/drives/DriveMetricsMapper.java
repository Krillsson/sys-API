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

package com.krillsson.sysapi.core.domain.drives;

import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DriveMetricsMapper {
    DriveMetricsMapper INSTANCE = Mappers.getMapper(DriveMetricsMapper.class);

    List<com.krillsson.sysapi.dto.drives.Drive> map(List<Drive> value);

    List<com.krillsson.sysapi.dto.drives.DriveLoad> mapLoads(List<DriveLoad> value);

    com.krillsson.sysapi.dto.drives.DriveLoad map(DriveLoad value);

    com.krillsson.sysapi.dto.drives.Drive map(Drive value);

    com.krillsson.sysapi.dto.sensors.HealthData map(HealthData value);

    com.krillsson.sysapi.dto.drives.OsPartition map(OsPartition value);

    com.krillsson.sysapi.dto.drives.DriveSpeed map(DriveSpeed value);

    com.krillsson.sysapi.dto.drives.DriveHealth map(DriveHealth value);

    com.krillsson.sysapi.dto.drives.Partition map(oshi.hardware.HWPartition value);

    com.krillsson.sysapi.dto.sensors.DataType map(DataType value);

    com.krillsson.sysapi.dto.drives.DriveValues map(com.krillsson.sysapi.core.domain.drives.DriveValues value);

    com.krillsson.sysapi.dto.drives.Partition map(com.krillsson.sysapi.core.domain.drives.Partition value);

    default String map(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    Map<String, List<com.krillsson.sysapi.dto.drives.DriveLoad>> mapLoadHistory(Map<LocalDateTime, List<com.krillsson.sysapi.dto.drives.DriveLoad>> history);
}
