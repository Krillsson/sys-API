package com.krillsson.sysapi.mdns

import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.util.EnvironmentUtils
import com.krillsson.sysapi.util.logger
import io.dropwizard.jetty.HttpConnectorFactory
import io.dropwizard.jetty.HttpsConnectorFactory
import io.dropwizard.lifecycle.Managed
import io.dropwizard.server.DefaultServerFactory
import io.dropwizard.server.SimpleServerFactory
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo


class Mdns(
    private val configuration: SysAPIConfiguration,
    private val connectivityCheckManager: ConnectivityCheckManager
) : Managed {

    private val logger by logger()

    lateinit var jmdns: JmDNS

    override fun start() {
        if (configuration.mDNS.enabled) {
            val address = connectivityCheckManager.findLocalIp()
            jmdns = JmDNS.create(address)
            connectorFactories()
                .mapNotNull {
                    when (it) {
                        is HttpsConnectorFactory -> "https" to it.port
                        is HttpConnectorFactory -> "http" to it.port
                        else -> null
                    }
                }
                // sorts https to be first
                .sortedByDescending { it.first.length }
                .forEach { (scheme, port) ->
                    val serviceType = "_$scheme._tcp.local"
                    val serviceName = "SysAPI-$scheme @ ${EnvironmentUtils.hostName}"
                    logger.info("Registering mDNS: $serviceType with name: $serviceName at port $port")
                    val serviceInfo = ServiceInfo.create(serviceType, serviceName, port, "GraphQL at /graphql")
                    jmdns.registerService(serviceInfo)
                }
        }
    }

    private fun connectorFactories() =
        ((configuration.serverFactory as? DefaultServerFactory)?.applicationConnectors.orEmpty() + (configuration.serverFactory as? SimpleServerFactory)?.connector)

    override fun stop() {
        if (configuration.mDNS.enabled) {
            jmdns.unregisterAllServices()
        }
    }

}