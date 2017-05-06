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

public class CpuLoad
{
    private final double cpuLoadCountingTicks;
    private final double cpuLoadOsMxBean;
    private final double user;
    private final double nice;
    private final double sys;
    private final double idle;
    private final double iowait;
    private final double irq;
    private final double softirq;

    public CpuLoad(double cpuLoadCountingTicks, double cpuLoadOsMxBean, double user, double nice, double sys, double idle, double iowait, double irq, double softirq)
    {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
        this.user = user;
        this.nice = nice;
        this.sys = sys;
        this.idle = idle;
        this.iowait = iowait;
        this.irq = irq;
        this.softirq = softirq;
    }

    public double getCpuLoadCountingTicks()
    {
        return cpuLoadCountingTicks;
    }

    public double getCpuLoadOsMxBean()
    {
        return cpuLoadOsMxBean;
    }

    public double getUser()
    {
        return user;
    }

    public double getNice()
    {
        return nice;
    }

    public double getSys()
    {
        return sys;
    }

    public double getIdle()
    {
        return idle;
    }

    public double getIowait()
    {
        return iowait;
    }

    public double getIrq()
    {
        return irq;
    }

    public double getSoftirq()
    {
        return softirq;
    }
}
