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
package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.metrics.cache.Cache
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetricsFactory
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetricsFactory
import com.krillsson.sysapi.core.metrics.windows.WindowsMetricsFactory
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import org.slf4j.LoggerFactory
import oshi.PlatformEnum
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

class MetricsFactory(
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem,
    private val platform: PlatformEnum,
    private val diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
    private val networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
    private val periodicTaskManager: TaskManager,
    private val connectivityCheckManager: ConnectivityCheckManager
) {
    fun get(configuration: SysAPIConfiguration): Metrics {

        val platformSpecific: Metrics = createPlatformSpecific(configuration)
        platformSpecific.initialize()

        return if (configuration.metricsConfig.cache.enabled) Cache.wrap(
            platformSpecific,
            configuration.metricsConfig.cache,
            platform.asPlatform(),
            operatingSystem.asOperatingSystem()
        ) else platformSpecific
    }

    private fun createPlatformSpecific(configuration: SysAPIConfiguration): Metrics {
        return when {
            platform == PlatformEnum.WINDOWS && (configuration.windows
                .enableOhmJniWrapper()) -> {
                LOGGER.info("Windows detected")
                val metrics = WindowsMetricsFactory.create(
                    operatingSystem,
                    hal,
                    platform,
                    periodicTaskManager,
                    diskReadWriteRateMeasurementManager,
                    networkUploadDownloadRateMeasurementManager,
                    connectivityCheckManager
                )
                if (metrics != null) {
                    metrics
                } else {
                    LOGGER.error("Unable to use Windows specific implementation: falling through to default one")
                    DefaultMetricsFactory.create(
                        operatingSystem,
                        hal,
                        platform,
                        periodicTaskManager,
                        diskReadWriteRateMeasurementManager,
                        networkUploadDownloadRateMeasurementManager,
                        connectivityCheckManager
                    )
                }
            }

            platform == PlatformEnum.LINUX && (operatingSystem.family.toLowerCase()
                .contains(RASPBIAN_QUALIFIER)) -> {
                LOGGER.info("Raspberry Pi detected")
                RaspbianMetricsFactory.create(
                    operatingSystem,
                    hal,
                    platform,
                    periodicTaskManager,
                    diskReadWriteRateMeasurementManager,
                    networkUploadDownloadRateMeasurementManager,
                    connectivityCheckManager
                )
            }

            else -> DefaultMetricsFactory.create(
                operatingSystem,
                hal,
                platform,
                periodicTaskManager,
                diskReadWriteRateMeasurementManager,
                networkUploadDownloadRateMeasurementManager,
                connectivityCheckManager
            )
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MetricsFactory::class.java)
        const val RASPBIAN_QUALIFIER = "raspbian"
    }
}