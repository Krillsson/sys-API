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
package com.krillsson.sysapi.core.domain.system

import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.network.NetworkInterface

class SystemInfo(
    val hostName: String,
    val operatingSystem: OperatingSystem,
    val platform: Platform,
    val cpuInfo: CpuInfo,
    val motherboard: Motherboard,
    val memory: MemoryLoad,
    val drives: List<Drive>,
    val networkInterfaces: List<NetworkInterface>,
    val gpus: List<Gpu>
) 