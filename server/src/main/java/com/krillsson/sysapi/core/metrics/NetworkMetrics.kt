package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import java.util.Optional

interface NetworkMetrics {
    fun networkInterfaces(): List<NetworkInterface>
    fun networkInterfaceById(id: String): Optional<NetworkInterface>
    fun networkInterfaceLoads(): List<NetworkInterfaceLoad>
    fun networkInterfaceLoadById(id: String): Optional<NetworkInterfaceLoad>
}