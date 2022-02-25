package com.krillsson.sysapi.client

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class Clients(externalIpCheckAddress: String) {
    val externalIpService = Retrofit.Builder()
        .baseUrl(externalIpCheckAddress)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(ExternalIpAddressService::class.java)
}