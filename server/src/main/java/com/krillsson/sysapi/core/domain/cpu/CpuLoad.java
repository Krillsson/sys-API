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
package com.krillsson.sysapi.core.domain.cpu;

import java.util.List;

public class CpuLoad {
    private final double cpuLoadCountingTicks;
    private final double cpuLoadOsMxBean;
    private final List<CoreLoad> coreLoads;
    private final CpuHealth cpuHealth;
    private final int processCount;
    private final int threadCount;

    public CpuLoad(double cpuLoadCountingTicks, double cpuLoadOsMxBean, List<CoreLoad> coreLoads, CpuHealth cpuHealth, int processCount, int threadCount) {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
        this.cpuHealth = cpuHealth;
        this.processCount = processCount;
        this.threadCount = threadCount;
        this.coreLoads = coreLoads;
    }

    public double getCpuLoadCountingTicks() {
        return cpuLoadCountingTicks;
    }

    public double getCpuLoadOsMxBean() {
        return cpuLoadOsMxBean;
    }

    public List<CoreLoad> getCoreLoads() {
        return coreLoads;
    }

    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    public int getProcessCount() {
        return processCount;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
