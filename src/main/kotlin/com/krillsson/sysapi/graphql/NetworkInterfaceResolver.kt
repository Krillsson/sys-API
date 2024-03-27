package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.metrics.Metrics
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class NetworkInterfaceResolver(val metrics: Metrics) : GraphQLResolver<NetworkInterface> {
    fun getId(networkInterface: NetworkInterface) = networkInterface.name

    fun getSpeed(networkInterface: NetworkInterface) = networkInterface.speedBitsPerSeconds
    fun getMetrics(networkInterface: NetworkInterface) =
        metrics.networkMetrics().networkInterfaceLoadById(networkInterface.name)
}