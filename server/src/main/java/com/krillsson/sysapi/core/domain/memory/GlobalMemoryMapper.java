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

package com.krillsson.sysapi.core.domain.memory;

import com.krillsson.sysapi.core.domain.system.DateMapper;
import com.krillsson.sysapi.dto.history.HistoryEntry;
import com.krillsson.sysapi.dto.processes.Memory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import oshi.hardware.GlobalMemory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {DateMapper.class}
)
public interface GlobalMemoryMapper {
    GlobalMemoryMapper INSTANCE = Mappers.getMapper(GlobalMemoryMapper.class);

    com.krillsson.sysapi.dto.processes.Memory map(oshi.hardware.GlobalMemory value);

    Map<String, Memory> mapHistory(Map<LocalDateTime, GlobalMemory> history);

    List<HistoryEntry<Memory>> mapHistory(List<com.krillsson.sysapi.core.history.HistoryEntry<GlobalMemory>> history);

}
