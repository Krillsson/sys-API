package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.graphql.domain.NetworkInterfaceReadWriteRate
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "NetworkInterfaceMetrics")
class NetworkInterfaceMetricResolver {

    @SchemaMapping
    fun id(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.name

    @SchemaMapping
    fun up(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.isUp
    @SchemaMapping
    fun bytesReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.bytesReceived

    @SchemaMapping
    fun bytesSent(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.bytesSent

    @SchemaMapping
    fun packetsReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.packetsReceived

    @SchemaMapping
    fun packetsSent(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.packetsSent

    @SchemaMapping
    fun inErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.inErrors

    @SchemaMapping
    fun outErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.outErrors

    @SchemaMapping
    fun readWriteRate(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.speed.let {
                NetworkInterfaceReadWriteRate(
                        it.receiveBytesPerSecond,
                        it.sendBytesPerSecond
                )
            }
}