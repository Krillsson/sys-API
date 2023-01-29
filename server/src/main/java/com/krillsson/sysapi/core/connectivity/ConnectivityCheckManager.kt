package com.krillsson.sysapi.core.connectivity

import com.krillsson.sysapi.client.ExternalIpAddressService
import com.krillsson.sysapi.config.ConnectivityCheckConfiguration
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.persistence.KeyValueRepository
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.logger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityCheckManager(
    private val externalIpAddressService: ExternalIpAddressService,
    private val repository: KeyValueRepository,
    private val connectivityCheckConfiguration: ConnectivityCheckConfiguration
) : Ticker.TickListener {

    private val logger by logger()

    private var _connectivity: Connectivity = defaultConnectivity

    override fun onTick() {
        if (connectivityCheckConfiguration.enabled) {
            _connectivity = resolveConnectivity()
        }
    }

    private fun resolveConnectivity(): Connectivity {
        val response = externalIpAddressService.getMyIp().execute()
        val localIp = findLocalIp()
        val storedExternalIp = repository.get(externalIpStoreKey)
        val newExternalIp = response.body()
        return if (response.isSuccessful && newExternalIp != null) {
            repository.put(externalIpStoreKey, newExternalIp)
            Connectivity(
                externalIp = newExternalIp,
                previousExternalIp = storedExternalIp,
                localIp = localIp.hostAddress,
                connected = true
            )
        } else {
            Connectivity(
                externalIp = null,
                previousExternalIp = storedExternalIp,
                localIp = localIp.hostAddress,
                connected = false
            )
        }
    }

    fun getConnectivity() = _connectivity

    fun findLocalIp(): InetAddress {
        val socket = Socket()
        return try {
            socket.connect(InetSocketAddress("google.com", 80))
            socket.localAddress
        } catch (exception: Exception) {
            val localHost = InetAddress.getLocalHost()
            logger.error("Exception while resolving local IP, resorting to $localHost", exception)
            localHost
        }
    }

    companion object {
        const val externalIpStoreKey = "externalIp"
        val defaultConnectivity = Connectivity(null, null, null, false)
    }
}