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
package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckService
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import ohmwrapper.NicInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import oshi.hardware.HardwareAbstractionLayer
import oshi.hardware.NetworkIF
import java.util.*

class WindowsNetworkMetrics(
    private val hal: HardwareAbstractionLayer,
    speedMeasurementManager: NetworkUploadDownloadRateMeasurementManager?,
    connectivityCheckService: ConnectivityCheckService?,
    private val monitorManager: DelegatingOHMManager
) : DefaultNetworkMetrics(
    hal, speedMeasurementManager!!, connectivityCheckService!!
) {
    override fun speedForInterfaceWithName(name: String): NetworkInterfaceSpeed {
        val networkOptional = hal.networkIFs.stream()
            .filter { n: NetworkIF -> name == n.name }
            .findAny()
        if (!networkOptional.isPresent) {
            throw NoSuchElementException(String.format("No NIC with id %s was found", name))
        }
        val networkIF = networkOptional.get()

        monitorManager.update()
        val networkMonitor = monitorManager.networkMonitor
        val nics = networkMonitor.nics
        val nicInfoOptional = Arrays.stream(nics)
            .filter { n: NicInfo -> networkIF.macaddr.equals(n.physicalAddress, ignoreCase = true) }
            .findAny()

        if (!nicInfoOptional.isPresent) {
            return EMPTY_INTERFACE_SPEED
        }
        val nicInfo = nicInfoOptional.get()
        return NetworkInterfaceSpeed(
            nicInfo.inBandwidth.value.toLong(),
            nicInfo.outBandwidth.value.toLong()
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WindowsNetworkMetrics::class.java)
    }
}
