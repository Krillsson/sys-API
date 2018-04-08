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

package com.krillsson.sysapi.core.domain.network;

import com.krillsson.sysapi.core.domain.system.DateMapper;
import com.krillsson.sysapi.core.domain.system.SystemInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {DateMapper.class}
)
public interface NetworkInterfacesMapper {
    NetworkInterfacesMapper INSTANCE = Mappers.getMapper(NetworkInterfacesMapper.class);

    com.krillsson.sysapi.dto.network.NetworkInterface map(NetworkInterface value);

    com.krillsson.sysapi.dto.network.NetworkInterfaceSpeed map(com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed value);

    com.krillsson.sysapi.dto.network.NetworkInterfaceLoad map(com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad value);

    List<com.krillsson.sysapi.dto.network.NetworkInterface> map(List<NetworkInterface> value);

    List<com.krillsson.sysapi.dto.network.NetworkInterfaceLoad> mapLoads(List<NetworkInterfaceLoad> value);

    Map<String, List<com.krillsson.sysapi.dto.network.NetworkInterfaceLoad>> mapLoadHistory(Map<LocalDateTime, List<NetworkInterfaceLoad>> history);

}
