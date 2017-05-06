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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NetworkInterfacesDataMapper {
    NetworkInterfacesDataMapper INSTANCE = Mappers.getMapper(NetworkInterfacesDataMapper.class);

    com.krillsson.sysapi.dto.network.NetworkInterfaceData map(com.krillsson.sysapi.core.domain.network.NetworkInterfaceData value);

    com.krillsson.sysapi.dto.network.NetworkInterfaceSpeed map(com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed value);

    com.krillsson.sysapi.dto.network.NetworkInterfaceData[] map(com.krillsson.sysapi.core.domain.network.NetworkInterfaceData[] value);

    @Mappings({
            @Mapping(target = "mtu", source = "MTU"),
            @Mapping(target = "ipv6addr", source = "IPv6addr"),
            @Mapping(target = "ipv4addr", source = "IPv4addr")
    })
    com.krillsson.sysapi.dto.network.NetworkIF map(oshi.hardware.NetworkIF value);

    com.krillsson.sysapi.dto.network.NetworkInterface map(java.net.NetworkInterface value) throws java.net.SocketException;
}
