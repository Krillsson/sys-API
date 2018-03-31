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

public class CpuHealth {
    private List<Double> temperatures;
    private double voltage;
    private double fanRpm;
    private double fanPercent;

    public CpuHealth(List<Double> temperatures, double voltage, double fanRpm, double fanPercent) {
        this.temperatures = temperatures;
        this.voltage = voltage;
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
    }

    public CpuHealth() {
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getFanRpm() {
        return fanRpm;
    }

    public double getFanPercent() {
        return fanPercent;
    }

    public void setTemperatures(List<Double> temperatures) {
        this.temperatures = temperatures;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public void setFanRpm(double fanRpm) {
        this.fanRpm = fanRpm;
    }

    public void setFanPercent(double fanPercent) {
        this.fanPercent = fanPercent;
    }
}
