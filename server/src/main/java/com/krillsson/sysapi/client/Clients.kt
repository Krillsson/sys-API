package com.krillsson.sysapi.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.core.connectivity.ExternalIpAddressService
import com.krillsson.sysapi.updatechecker.GithubApiService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Clients(mapper: ObjectMapper, externalIpCheckAddress: String, githubAddress: String) {
    val externalIpService = Retrofit.Builder()
        .baseUrl(externalIpCheckAddress)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(ExternalIpAddressService::class.java)

    val githubApiService = Retrofit.Builder()
        .baseUrl(githubAddress)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .build()
        .create(GithubApiService::class.java)
}