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
package com.krillsson.sysapi.core.metrics.windows.util

import ohmwrapper.OHMSensor
import java.util.*

object NullSafeOhmMonitor {
    val DEFAULT_SENSOR: NullSafeOHMSensor = NullSafeOHMSensor()

    fun nullSafe(sensor: OHMSensor?): OHMSensor {
        return sensor ?: NullSafeOHMSensor()
    }

    fun nullSafe(arr: Array<OHMSensor?>?): Array<OHMSensor?> {
        return arr ?: arrayOfNulls(0)
    }

    fun nullSafeGetValue(sensor: OHMSensor?): Double {
        return Optional.ofNullable(sensor).orElse(DEFAULT_SENSOR).value
    }

    class NullSafeOHMSensor : OHMSensor(null, null, null, false) {
        override fun Text(): String {
            return "N/A"
        }

        override fun getValue(): Double {
            return (-1).toDouble()
        }
    }
}
