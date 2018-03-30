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
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultInfoProviderFactory;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.PlatformEnum;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static com.krillsson.sysapi.core.metrics.rasbian.RaspbianLinuxInfoProvider.RASPBIAN_QUALIFIER;

public class InfoProviderFactory {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(InfoProviderFactory.class);


    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final PlatformEnum os;
    private final SystemApiConfiguration configuration;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    public InfoProviderFactory(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, PlatformEnum os, SystemApiConfiguration configuration, SpeedMeasurementManager speedMeasurementManager) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.os = os;
        this.configuration = configuration;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = new Utils();
    }

    public DefaultInfoProviderFactory provide() {
        switch (os) {
            case WINDOWS:
                if (configuration.windows() == null || configuration.windows().enableOhmJniWrapper()) {

                }
                break;
            //falling through to default case
            case LINUX:
                if (operatingSystem.getFamily().toLowerCase().contains(RASPBIAN_QUALIFIER)) {
                    LOGGER.info("Raspberry Pi detected");
                }
                break;
            case MACOSX:
                //https://github.com/Chris911/iStats
            case FREEBSD:
            case SOLARIS:
            case UNKNOWN:
            default:
                break;
        }
    }

}
