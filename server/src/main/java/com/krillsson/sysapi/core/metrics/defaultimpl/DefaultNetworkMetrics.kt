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

import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceValues
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.CurrentSpeed
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager.SpeedSource
import org.slf4j.LoggerFactory
import oshi.hardware.HardwareAbstractionLayer
import oshi.hardware.NetworkIF
import java.net.SocketException
import java.util.Optional

open class DefaultNetworkMetrics(
    private val hal: HardwareAbstractionLayer,
    private val speedMeasurementManager: SpeedMeasurementManager
) : NetworkMetrics {

    class NetworkSpeedSource(private val networkIF: NetworkIF) : SpeedSource {
        override fun getName(): String {
            return networkIF.name
        }

        override fun getCurrentRead(): Long {
            networkIF.updateAttributes()
            return networkIF.bytesRecv
        }

        override fun getCurrentWrite(): Long {
            networkIF.updateAttributes()
            return networkIF.bytesSent
        }
    }

    fun register() {
        speedMeasurementManager.register(
            hal.networkIFs.map {
                NetworkSpeedSource(it)
            }
        )
    }

    override fun networkInterfaces(): List<NetworkInterface> {
        return hal.networkIFs
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
        return Optional.ofNullable(hal.networkIFs.firstOrNull {
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
        return hal.networkIFs.map {
            var up = false
            try {
                up = it.queryNetworkInterface().isUp
            } catch (e: SocketException) {
                LOGGER.error("Error occurred while getting status for NIC", e)
            }
            it.asNetworkInterfaceLoad(up, speedForInterfaceWithName(it.name))
        }.toList()
    }

    override fun networkInterfaceLoadById(id: String): Optional<NetworkInterfaceLoad> {
        return Optional.ofNullable(hal.networkIFs.firstOrNull {
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
                    LOGGER.error("Error occurred while getting status for NIC", e)
                }
                it.asNetworkInterfaceLoad(up, speedForInterfaceWithName(it.name))
            })
    }

    protected open fun speedForInterfaceWithName(name: String?): NetworkInterfaceSpeed {
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
        private const val BYTE_TO_BIT = 8
    }
}