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
package com.krillsson.sysapi.core.domain.gpu;

public class GpuHealth {
    private double fanRpm;
    private double fanPercent;
    private double temperature;
    private double coreLoad;
    private double memoryLoad;

    public GpuHealth(double fanRpm, double fanPercent, double temperature, double coreLoad, double memoryLoad) {
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.temperature = temperature;
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
    }

    public double getFanRpm() {
        return fanRpm;
    }

    public double getFanPercent() {
        return fanPercent;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCoreLoad() {
        return coreLoad;
    }

    public double getMemoryLoad() {
        return memoryLoad;
    }
}
