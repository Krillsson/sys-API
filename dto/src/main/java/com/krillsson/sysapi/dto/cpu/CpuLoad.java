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
package com.krillsson.sysapi.dto.cpu;

import java.util.List;

public class CpuLoad {
    private double cpuLoadCountingTicks;
    private double cpuLoadOsMxBean;
    private double systemLoadAverage;
    private List<CoreLoad> coreLoads;
    private CpuHealth cpuHealth;
    private int processCount;
    private int threadCount;

    public CpuLoad(double cpuLoadCountingTicks, double cpuLoadOsMxBean, double systemLoadAverage, List<CoreLoad> coreLoads, CpuHealth cpuHealth, int processCount, int threadCount) {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
        this.systemLoadAverage = systemLoadAverage;
        this.cpuHealth = cpuHealth;
        this.processCount = processCount;
        this.threadCount = threadCount;
        this.coreLoads = coreLoads;
    }

    public CpuLoad() {
    }

    public double getCpuLoadCountingTicks() {
        return cpuLoadCountingTicks;
    }

    public void setCpuLoadCountingTicks(double cpuLoadCountingTicks) {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
    }

    public double getCpuLoadOsMxBean() {
        return cpuLoadOsMxBean;
    }

    public void setCpuLoadOsMxBean(double cpuLoadOsMxBean) {
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public List<CoreLoad> getCoreLoads() {
        return coreLoads;
    }

    public void setCoreLoads(List<CoreLoad> coreLoads) {
        this.coreLoads = coreLoads;
    }

    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    public void setCpuHealth(CpuHealth cpuHealth) {
        this.cpuHealth = cpuHealth;
    }

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
