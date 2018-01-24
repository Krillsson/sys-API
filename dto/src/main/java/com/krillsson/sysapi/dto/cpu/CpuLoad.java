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
        "cpuLoadCountingTicks",
        "cpuLoadOsMxBean",
        "coreLoads",
})
public class CpuLoad {

    @JsonProperty("cpuLoadCountingTicks")
    private double cpuLoadCountingTicks;
    @JsonProperty("cpuLoadOsMxBean")
    private double cpuLoadOsMxBean;
    @JsonProperty("coreLoads")
    private CoreLoad[] coreLoads;

    public CpuLoad() {

    }

    public CpuLoad(double cpuLoadCountingTicks, double cpuLoadOsMxBean, CoreLoad[] coreLoads) {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
        this.coreLoads = coreLoads;
    }

    @JsonProperty("cpuLoadCountingTicks")
    public double getCpuLoadCountingTicks() {
        return cpuLoadCountingTicks;
    }

    @JsonProperty("cpuLoadCountingTicks")
    public void setCpuLoadCountingTicks(double cpuLoadCountingTicks) {
        this.cpuLoadCountingTicks = cpuLoadCountingTicks;
    }

    @JsonProperty("cpuLoadOsMxBean")
    public double getCpuLoadOsMxBean() {
        return cpuLoadOsMxBean;
    }

    @JsonProperty("cpuLoadOsMxBean")
    public void setCpuLoadOsMxBean(double cpuLoadOsMxBean) {
        this.cpuLoadOsMxBean = cpuLoadOsMxBean;
    }

    @JsonProperty("coreLoads")
    public CoreLoad[] getCoreLoads() {
        return coreLoads;
    }

    @JsonProperty("coreLoads")
    public void setCoreLoads(CoreLoad[] coreLoads) {
        this.coreLoads = coreLoads;
    }
}
