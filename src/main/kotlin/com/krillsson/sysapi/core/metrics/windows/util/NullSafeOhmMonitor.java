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
package com.krillsson.sysapi.core.metrics.windows.util;

import ohmwrapper.OHMSensor;

import java.util.Optional;

public class NullSafeOhmMonitor {

    public static final NullSafeOHMSensor DEFAULT_SENSOR = new NullSafeOHMSensor();

    public static OHMSensor nullSafe(OHMSensor sensor) {
        if (sensor != null) {
            return sensor;
        } else return new NullSafeOHMSensor();
    }

    public static OHMSensor[] nullSafe(OHMSensor[] arr) {
        if (arr != null) {
            return arr;
        } else {
            return new OHMSensor[0];
        }
    }

    public static double nullSafeGetValue(OHMSensor sensor) {
        return Optional.ofNullable(sensor).orElse(DEFAULT_SENSOR).getValue();
    }

    public static class NullSafeOHMSensor extends OHMSensor {
        public NullSafeOHMSensor() {
            super(null, null, null, false);
        }

        @Override
        public String Text() {
            return "N/A";
        }

        @Override
        public double getValue() {
            return -1;
        }
    }
}
