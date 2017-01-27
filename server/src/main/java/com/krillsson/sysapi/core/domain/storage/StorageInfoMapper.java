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

package com.krillsson.sysapi.core.domain.storage;

import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import oshi.software.os.OSFileStore;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StorageInfoMapper {
    StorageInfoMapper INSTANCE = Mappers.getMapper(StorageInfoMapper.class);

    com.krillsson.sysapi.dto.storage.StorageInfo map(StorageInfo value);

    @Mappings(
            @Mapping(source = "hwDiskStore", target = "diskStore")
    )
    com.krillsson.sysapi.dto.storage.DiskInfo map(DiskInfo value);

    com.krillsson.sysapi.dto.sensors.HealthData map(HealthData value);

    @Mappings(
            @Mapping(source = "UUID", target = "uuid")
    )
    com.krillsson.sysapi.dto.storage.OsFileStore map(OSFileStore value);

    com.krillsson.sysapi.dto.storage.DiskHealth map(DiskHealth value);

    com.krillsson.sysapi.dto.storage.DiskStore map(oshi.hardware.HWDiskStore value);

    com.krillsson.sysapi.dto.storage.Partition map(oshi.hardware.HWPartition value);

    com.krillsson.sysapi.dto.sensors.DataType map(DataType value);
}
