package com.krillsson.sysapi.core.connectivity

import com.krillsson.sysapi.client.ExternalIpAddressService
import com.krillsson.sysapi.config.ConnectivityCheckConfiguration
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.persistence.KeyValueRepository
import com.krillsson.sysapi.util.Ticker

class ConnectivityCheckManager(
    private val externalIpAddressService: ExternalIpAddressService,
    private val repository: KeyValueRepository,
    private val connectivityCheckConfiguration: ConnectivityCheckConfiguration
) : Ticker.TickListener {

    private var _connectivity: Connectivity = defaultConnectivity

    override fun onTick() {
        if(connectivityCheckConfiguration.enabled) {
            _connectivity = resolveConnectivity()
        }
    }

    private fun resolveConnectivity(): Connectivity {
        val response = externalIpAddressService.getMyIp().execute()
        val storedExternalIp = repository.get(externalIpStoreKey)
        val storedPreviousExternalIp = repository.get(previousExternalIpKey)
        val newExternalIp = response.body()
        return if (response.isSuccessful && newExternalIp != null) {
            val previousIp = if (storedExternalIp != null && storedExternalIp != newExternalIp) {
                repository.put(previousExternalIpKey, storedPreviousExternalIp)
                storedExternalIp
            } else {
                storedPreviousExternalIp
            }
            repository.put(externalIpStoreKey, newExternalIp)
            Connectivity(
                externalIp = newExternalIp,
                previousExternalIp = previousIp,
                connected = true
            )
        } else {
            Connectivity(
                externalIp = null,
                previousExternalIp = storedExternalIp,
                connected = false
            )
        }
    }

    fun getConnectivity() = _connectivity

    companion object {
        const val externalIpStoreKey = "externalIp"
        const val previousExternalIpKey = "previousExternalIp"
        val defaultConnectivity = Connectivity(null, null, false)
    }
}