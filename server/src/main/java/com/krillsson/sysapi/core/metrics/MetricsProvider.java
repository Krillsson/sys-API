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
package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.config.MetricsConfiguration;
import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.metrics.cache.Cache;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetricsFactory;
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetricsFactory;
import com.krillsson.sysapi.core.metrics.windows.MonitorManagerFactory;
import com.krillsson.sysapi.core.metrics.windows.WindowsMetricsFactory;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.PlatformEnum;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import static com.krillsson.sysapi.core.metrics.rasbian.RaspbianCpuMetrics.RASPBIAN_QUALIFIER;

public class MetricsProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MetricsProvider.class);

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final PlatformEnum os;
    private final SystemApiConfiguration configuration;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    public MetricsProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, PlatformEnum os, SystemApiConfiguration configuration, SpeedMeasurementManager speedMeasurementManager) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.os = os;
        this.configuration = configuration;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = new Utils();
    }

    public MetricsFactory create() {
        switch (os) {
            case WINDOWS:
                if (configuration.windows() == null || configuration.windows().enableOhmJniWrapper()) {
                    LOGGER.info("Windows detected");
                    WindowsMetricsFactory windowsMetricsFactory = new WindowsMetricsFactory(
                            new MonitorManagerFactory(),
                            hal,
                            operatingSystem,
                            speedMeasurementManager,
                            utils
                    );
                    if (windowsMetricsFactory.prerequisitesFilled()) {
                        windowsMetricsFactory.initialize();
                        return Cache.wrap(windowsMetricsFactory, configuration.metrics().getCache());
                    }
                    LOGGER.error("Unable to use Windows specific implementation: falling through to default one");
                }
                break;
            case LINUX:
                if (operatingSystem.getFamily().toLowerCase().contains(RASPBIAN_QUALIFIER)) {
                    LOGGER.info("Raspberry Pi detected");
                    RaspbianMetricsFactory raspbianMetricsFactory = new RaspbianMetricsFactory(
                            hal,
                            operatingSystem,
                            speedMeasurementManager,
                            utils
                    );
                    if (raspbianMetricsFactory.prerequisitesFilled()) {
                        raspbianMetricsFactory.initialize();
                        return Cache.wrap(raspbianMetricsFactory, configuration.metrics().getCache());
                    }
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
        DefaultMetricsFactory metricsFactory = new DefaultMetricsFactory(
                hal,
                operatingSystem,
                speedMeasurementManager,
                utils
        );
        if (metricsFactory.prerequisitesFilled()) {
            metricsFactory.initialize();
            return Cache.wrap(metricsFactory, configuration.metrics().getCache());
        } else {
            throw new IllegalStateException("No metrics factory available");
        }
    }

}
