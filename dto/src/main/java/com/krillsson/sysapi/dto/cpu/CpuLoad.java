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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user",
        "nice",
        "sys",
        "idle",
        "iowait",
        "irq",
        "softirq"
})
public class CpuLoad
{

    @JsonProperty("cpuLoadCountingTicks")
    private double cpuLoadCountingTicks;
    @JsonProperty("cpuLoadOsMxBean")
    private double cpuLoadOsMxBean;

    @JsonProperty("user")
    private double user;
    @JsonProperty("nice")
    private double nice;
    @JsonProperty("sys")
    private double sys;
    @JsonProperty("idle")
    private double idle;
    @JsonProperty("iowait")
    private double iowait;
    @JsonProperty("irq")
    private double irq;
    @JsonProperty("softirq")
    private double softirq;

    public CpuLoad()
    {

    }

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

    @JsonProperty("cpuLoadCountingTicks")
    public double getCpuLoadCountingTicks()
    {
        return cpuLoadCountingTicks;
    }

    @JsonProperty("cpuLoadCountingTicks")
    public void setCpuLoadCountingTicks(double cpuLoadCountingTicks)
    {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
    }

    @JsonProperty("cpuLoadOsMxBean")
    public double getCpuLoadOsMxBean()
    {
        return cpuLoadOsMxBean;
    }

    @JsonProperty("cpuLoadOsMxBean")
    public void setCpuLoadOsMxBean(double cpuLoadOsMxBean)
    {
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
    }

    @JsonProperty("user")
    public double getUser()
    {
        return user;
    }

    @JsonProperty("user")
    public void setUser(double user)
    {
        this.user = user;
    }

    @JsonProperty("nice")
    public double getNice()
    {
        return nice;
    }

    @JsonProperty("nice")
    public void setNice(double nice)
    {
        this.nice = nice;
    }

    @JsonProperty("sys")
    public double getSys()
    {
        return sys;
    }

    @JsonProperty("sys")
    public void setSys(double sys)
    {
        this.sys = sys;
    }

    @JsonProperty("idle")
    public double getIdle()
    {
        return idle;
    }

    @JsonProperty("idle")
    public void setIdle(double idle)
    {
        this.idle = idle;
    }

    @JsonProperty("iowait")
    public double getIowait()
    {
        return iowait;
    }

    @JsonProperty("iowait")
    public void setIowait(double iowait)
    {
        this.iowait = iowait;
    }

    @JsonProperty("irq")
    public double getIrq()
    {
        return irq;
    }

    @JsonProperty("irq")
    public void setIrq(double irq)
    {
        this.irq = irq;
    }

    @JsonProperty("softirq")
    public double getSoftirq()
    {
        return softirq;
    }

    @JsonProperty("softirq")
    public void setSoftirq(double softirq)
    {
        this.softirq = softirq;
    }
}
