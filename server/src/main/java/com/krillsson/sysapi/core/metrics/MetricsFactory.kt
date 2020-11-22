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

import com.google.common.annotations.VisibleForTesting
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.metrics.cache.Cache
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianCpuMetrics
import com.krillsson.sysapi.core.metrics.rasbian.RaspbianMetrics
import com.krillsson.sysapi.core.metrics.windows.WindowsMetricsFactory
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
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
    private val speedMeasurementManager: SpeedMeasurementManager,
    private val ticker: Ticker
) {
    private val utils: Utils = Utils()
    private var cache = true

    private var metrics: Metrics? = null

    fun get(configuration: SysAPIConfiguration): Metrics {
        if (metrics != null) return metrics!!

        val platformSpecific: Metrics = createPlatformSpecific(configuration)
        platformSpecific.initialize()
        metrics = if (cache) Cache.wrap(
            platformSpecific,
            configuration.metrics().cache,
            platform.asPlatform(),
            operatingSystem.asOperatingSystem()
        ) else platformSpecific

        return metrics!!
    }

    private fun createPlatformSpecific(configuration: SysAPIConfiguration): Metrics {
        return when {
            platform == PlatformEnum.WINDOWS && (configuration.windows() == null || configuration.windows()
                .enableOhmJniWrapper()) -> {
                LOGGER.info("Windows detected")
                WindowsMetricsFactory.create(operatingSystem, hal, platform, ticker, utils, speedMeasurementManager)
            }
            platform == PlatformEnum.LINUX && (operatingSystem.family.toLowerCase()
                .contains(RaspbianCpuMetrics.RASPBIAN_QUALIFIER)) -> {
                LOGGER.info("Raspberry Pi detected")
                RaspbianMetrics(
                    hal,
                    operatingSystem,
                    speedMeasurementManager,
                    ticker,
                    utils
                )
            }
            else -> null
        } ?: return DefaultMetrics(
            hal,
            operatingSystem,
            speedMeasurementManager,
            ticker,
            utils
        )
    }

    @VisibleForTesting
    fun setCache(cache: Boolean) {
        this.cache = cache
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MetricsFactory::class.java)
    }
}