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

package com.krillsson.sysapi.core.domain.processes;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import oshi.hardware.GlobalMemory;

import java.util.List;

public class ProcessesInfo {
    private final MemoryLoad memory;
    private final long processId;
    private final long threadCount;
    private final long processCount;
    private final List<Process> processes;

    public ProcessesInfo(MemoryLoad memory, long processId, long threadCount, long processCount, List<Process> processes) {
        this.memory = memory;
        this.processId = processId;
        this.threadCount = threadCount;
        this.processCount = processCount;
        this.processes = processes;
    }

    public MemoryLoad getMemory() {
        return memory;
    }

    public long getProcessId() {
        return processId;
    }

    public long getProcessCount() {
        return processCount;
    }

    public long getThreadCount() {
        return threadCount;
    }

    public List<Process> getProcesses() {
        return processes;
    }
}
