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
package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.domain.network.*
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.CurrentSpeed
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.SpeedSource
import org.slf4j.LoggerFactory
import oshi.hardware.HardwareAbstractionLayer
import oshi.hardware.NetworkIF
import java.net.SocketException
import java.util.*

open class DefaultNetworkMetrics(
    private val hal: HardwareAbstractionLayer,
    private val speedMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
    private val connectivityCheckManager: ConnectivityCheckManager
) : NetworkMetrics {

    private val networkInterfaces: MutableList<NetworkIF> = hal.networkIFs

    class NetworkSpeedSource(private val networkIF: NetworkIF) : SpeedSource {

        override fun currentRead(): Long {
            return networkIF.bytesRecv
        }

        override fun currentWrite(): Long {
            return networkIF.bytesSent
        }

        override val name: String
            get() = networkIF.name

        override fun update() {
            networkIF.updateAttributes()
        }
    }

    fun register() {
        speedMeasurementManager.register(
            networkInterfaces.map {
                NetworkSpeedSource(it)
            }
        )
    }

    override fun connectivity(): Connectivity {
        return connectivityCheckManager.getConnectivity()
    }

    override fun networkInterfaces(): List<NetworkInterface> {
        return networkInterfaces
            .map {
                var isLoopback = false
                try {
                    isLoopback = it.queryNetworkInterface().isLoopback
                } catch (e: SocketException) {
                    //ignore
                    LOGGER.warn("Socket exception while queering for loopback parameter", e)
                }
                it.asNetworkInterface(isLoopback)
            }.toList()
    }

    override fun networkInterfaceById(id: String): Optional<NetworkInterface> {
        return Optional.ofNullable(networkInterfaces.firstOrNull {
            it.name.equals(
                id,
                ignoreCase = true
            )
        }
            ?.let {
                var isLoopback = false
                try {
                    isLoopback = it.queryNetworkInterface().isLoopback
                } catch (e: SocketException) {
                    //ignore
                    LOGGER.warn("Socket exception while queering for loopback parameter", e)
                }
                it.asNetworkInterface(isLoopback)
            })
    }

    override fun networkInterfaceLoads(): List<NetworkInterfaceLoad> {
        return networkInterfaces.map {
            var up = false
            try {
                up = it.queryNetworkInterface().isUp
            } catch (e: SocketException) {
                LOGGER.warn("Error occurred while getting status for NIC", e)
            }
            it.asNetworkInterfaceLoad(up, speedForInterfaceWithName(it.name))
        }.toList()
    }

    override fun networkInterfaceLoadById(id: String): Optional<NetworkInterfaceLoad> {
        return Optional.ofNullable(networkInterfaces.firstOrNull {
            it.name.equals(
                id,
                ignoreCase = true
            )
        }
            ?.let {
                var up = false
                try {
                    up = it.queryNetworkInterface().isUp
                } catch (e: SocketException) {
                    LOGGER.warn("Error occurred while getting status for NIC", e)
                }
                it.asNetworkInterfaceLoad(up, speedForInterfaceWithName(it.name))
            })
    }

    protected open fun speedForInterfaceWithName(name: String): NetworkInterfaceSpeed {
        val currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(
            name
        )
        return currentSpeedForName.map { s: CurrentSpeed ->
            NetworkInterfaceSpeed(
                s.readPerSeconds,
                s.writePerSeconds
            )
        }.orElse(EMPTY_INTERFACE_SPEED)
    }

    private fun NetworkIF.asNetworkInterface(isLoopback: Boolean): NetworkInterface =
        NetworkInterface(
            name,
            displayName,
            macaddr,
            speed,
            mtu,
            isLoopback,
            iPv4addr.asList(),
            iPv6addr.asList()
        )

    private fun NetworkIF.asNetworkInterfaceLoad(
        up: Boolean,
        nicSpeed: NetworkInterfaceSpeed
    ): NetworkInterfaceLoad = NetworkInterfaceLoad(
        name,
        macaddr,
        up,
        NetworkInterfaceValues(
            speed,
            bytesRecv,
            bytesSent,
            packetsRecv,
            packetsSent,
            inErrors,
            outErrors
        ),
        nicSpeed
    )

    companion object {
        @JvmField
        protected val EMPTY_INTERFACE_SPEED = NetworkInterfaceSpeed(0, 0)
        private val LOGGER = LoggerFactory.getLogger(
            DefaultNetworkMetrics::class.java
        )
    }
}