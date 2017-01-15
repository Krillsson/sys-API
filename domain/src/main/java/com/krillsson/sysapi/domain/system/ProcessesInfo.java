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
package com.krillsson.sysapi.domain.system;

import oshi.json.hardware.GlobalMemory;
import oshi.json.software.os.OSProcess;

public class ProcessesInfo {
    private final GlobalMemory memory;
    private final OSProcess[] processes;

    public ProcessesInfo(GlobalMemory memory, OSProcess[] processes) {
        this.memory = memory;
        this.processes = processes;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public OSProcess[] getProcesses() {
        return processes;
    }
}
