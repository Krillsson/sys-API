package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.graphql.domain.NetworkInterfaceReadWriteRate
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class NetworkInterfaceMetricResolver : GraphQLResolver<NetworkInterfaceLoad> {
    fun getNetworkInterfaceid(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.name

    fun getBytesReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.bytesReceived

    fun getBytesSent(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.bytesSent

    fun getPacketsReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.packetsReceived

    fun getPacketsSent(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.packetsSent

    fun getInErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.inErrors

    fun getOutErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.values.outErrors

    fun getReadWriteRate(networkInterfaceLoad: NetworkInterfaceLoad) =
        networkInterfaceLoad.speed.let {
            NetworkInterfaceReadWriteRate(
                it.receiveBytesPerSecond,
                it.sendBytesPerSecond
            )
        }
}