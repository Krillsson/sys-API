package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.metrics.Metrics
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName="NetworkInterface")
class NetworkInterfaceResolver(val metrics: Metrics) {
    @SchemaMapping
    fun id(networkInterface: NetworkInterface) = networkInterface.name

    @SchemaMapping
    fun speed(networkInterface: NetworkInterface) = networkInterface.speedBitsPerSeconds
    @SchemaMapping
    fun metrics(networkInterface: NetworkInterface) =
            metrics.networkMetrics().networkInterfaceLoadById(networkInterface.name)
}