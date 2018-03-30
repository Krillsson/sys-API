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
package com.krillsson.sysapi.core.metrics.rasbian;

import com.google.common.annotations.VisibleForTesting;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDiskProvider;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkProvider;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.ExecutingCommand;
import oshi.util.FileUtil;


public class RaspbianLinuxInfoProvider extends DefaultInfoProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RaspbianLinuxInfoProvider.class);

    public static final String RASPBIAN_QUALIFIER = "raspbian";

    private static final String CPU_TEMP_FILE_LOCATION = "/sys/class/thermal/thermal_zone0/temp";
    private static final String VCGENCMD = "vcgencmd";
    private static final String VCGENCMD_VOLT = VCGENCMD + "measure_volts core";


    public RaspbianLinuxInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils, DefaultNetworkProvider defaultNetworkProvider, DefaultDiskProvider defaultDiskProvider) {
        super(hal, operatingSystem, utils, defaultNetworkProvider, defaultDiskProvider);
    }

    @Override
    public double cpuVoltage() {
        String vcgenCmdAnswer = executeCommand();
        if (vcgenCmdAnswer != null && vcgenCmdAnswer.length() > 1) {
            String trimmed = vcgenCmdAnswer.replace("volt=", "").replace("V", "");
            try {
                Double value = Double.valueOf(trimmed);
                if (value.isInfinite() || value.isNaN()) {
                    return 0.0;
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                LOGGER.error("Error while parsing vcgencmd command", e);
                return 0.0;
            }
        }
        return super.cpuVoltage();
    }

    @Override
    public double[] cpuTemperatures() {
        return new double[]{FileUtil.getLongFromFile(CPU_TEMP_FILE_LOCATION) / 1000.0};
    }

    @VisibleForTesting
    String executeCommand() {
        return ExecutingCommand.getFirstAnswer(VCGENCMD_VOLT);
    }
}
