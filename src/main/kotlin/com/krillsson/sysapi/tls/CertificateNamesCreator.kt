package com.krillsson.sysapi.tls

import com.krillsson.sysapi.config.SelfSignedCertificateConfiguration
import com.krillsson.sysapi.core.connectivity.ExternalIpAddressService
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import org.springframework.stereotype.Component

@Component
class CertificateNamesCreator(
    private val networkMetrics: NetworkMetrics,
    private val service: ExternalIpAddressService
) {

    data class Names(
        val commonName: String,
        val subjectAlternativeNames: List<String>
    )

    fun createNames(config: SelfSignedCertificateConfiguration): Names {

        return if (config.populateCN || config.populateSAN) {
            val response = service.getMyIp().execute()
            val cn = if (config.populateCN) response.body() ?: config.commonName else config.commonName
            val san = if (config.populateSAN) networkMetrics.networkInterfaces()
                .flatMap { it.ipv4 } else config.subjectAlternativeNames
            Names(cn, san)
        } else {
            Names(config.commonName, config.subjectAlternativeNames)
        }

    }
}