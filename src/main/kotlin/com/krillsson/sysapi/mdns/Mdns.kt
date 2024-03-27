package com.krillsson.sysapi.mdns

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.util.EnvironmentUtils
import com.krillsson.sysapi.util.logger
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.getProperty
import org.springframework.stereotype.Service
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

@Service
class Mdns(
    private val configuration: YAMLConfigFile,
    private val connectivityCheckManager: ConnectivityCheckManager
) {

    private val logger by logger()

    lateinit var jmdns: JmDNS

    @Autowired
    private lateinit var env: Environment

    @PostConstruct
    fun start() {
        if (configuration.mDNS.enabled) {
            val address = connectivityCheckManager.findLocalIp()
            jmdns = JmDNS.create(address)
            listOf(
                "http" to env.getProperty<Int>("http.port", 8080),
                "https" to env.getProperty<Int>("server.port", 8443),
            )
                // sorts https to be first
                .sortedByDescending { it.first.length }
                .forEach { (scheme, port) ->
                    val serviceType = "_$scheme._tcp.local"
                    val serviceName = "${EnvironmentUtils.hostName} - sys-API $scheme"
                    logger.info("Registering mDNS: $serviceType with name: $serviceName at port $port")
                    val serviceInfo = ServiceInfo.create(serviceType, serviceName, port, "GraphQL at /graphql")
                    jmdns.registerService(serviceInfo)
                }
        }
    }

    @PreDestroy
    fun stop() {
        if (configuration.mDNS.enabled) {
            jmdns.unregisterAllServices()
        }
    }
}